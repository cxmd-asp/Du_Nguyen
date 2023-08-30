package com.example.safarione.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.safarione.injection.InjectionNamed
import com.example.safarione.interactor.chat.IncomingMessage
import com.example.safarione.interactor.chat.OutgoingMessage
import com.example.safarione.interactor.chat.SaveIncomingMessageUseCase
import com.example.safarione.interactor.chat.SaveOutgoingMessageUseCase
import com.example.safarione.model.chat.message.Message
import com.example.safarione.model.status.MessageStatus
import com.example.safarione.module.credential.CredentialManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatInterceptorService : Service(), ChatInterceptor {

    inner class ChatInterceptorBinder: Binder() {

        fun getInterceptor() : ChatInterceptor = this@ChatInterceptorService
    }

    private val binder = ChatInterceptorBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    })

    private val stateOfConnectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    @Inject
    lateinit var credentialManager: CredentialManager

    @Inject
    @Named(InjectionNamed.XMPP_DOMAIN)
    lateinit var xmppDomain: String

    @Inject
    lateinit var chatServiceConfiguration: XMPPTCPConnectionConfiguration

    @Inject
    lateinit var saveIncomingMessageUseCase: SaveIncomingMessageUseCase

    @Inject
    lateinit var saveOutgoingMessageUseCase: SaveOutgoingMessageUseCase

    private val stateOfChatManager = MutableStateFlow<ChatManager?>(null)

    private val connection: XMPPTCPConnection by lazy {
        XMPPTCPConnection(chatServiceConfiguration)
    }

    override val isConnected: StateFlow<ConnectionStatus> = stateOfConnectionStatus.asStateFlow()

    override fun sendToUser(toId: Long, message: String) {
        scope.launch {
            val manager = stateOfChatManager.value ?: return@launch
            val chat = manager.chatWith(JidCreate.entityBareFrom("${toId}@${xmppDomain}"))
            chat.send(message)
            saveOutgoingMessageUseCase.run(
                OutgoingMessage(
                    toId, message
                )
            )
        }
    }

    override fun sendToRoom(roomId: Long, message: String) {

    }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            credentialManager.credential.collectLatest {
                if (it == null) {
                    stateOfConnectionStatus.value = ConnectionStatus.DISCONNECTED
                    stateOfChatManager.value = null
                    connection.disconnect()
                } else {
                    stateOfConnectionStatus.value = ConnectionStatus.CONNECTING
                    connection.connect()
                    connection.login(it.username, it.password)
                    stateOfChatManager.value = ChatManager.getInstanceFor(connection)
                    stateOfConnectionStatus.value = ConnectionStatus.CONNECTED
                }
            }
        }
        scope.launch {
            stateOfChatManager.flatMapLatest {
                Timber.d("Start listening incoming message from $it ")
                it?.flowOfIncomingMessages() ?: emptyFlow()
            }.collect {
                saveIncomingMessageUseCase.run(
                    IncomingMessage(
                        it.first,
                        it.second
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        connection.disconnect()
    }
}

private fun ChatManager.flowOfIncomingMessages() : Flow<Pair<Long, Message>> = callbackFlow {
    val listener = IncomingChatMessageListener { from, remoteMessage, _ ->
        val userId = from.toString().split("@").first().toLong()
        Timber.d("IncomingChatMessageListener $userId :: $remoteMessage ")
        val message = Message(
            senderId = userId,
            message = remoteMessage.body,
            status = MessageStatus.RECEIVED
        )
        trySend(
            userId to message
        )
    }
    Timber.d("Add listening incoming message")
    addIncomingListener(listener)
    awaitClose {
        Timber.d("Remove listening incoming message")
        removeIncomingListener(listener)
    }
}
