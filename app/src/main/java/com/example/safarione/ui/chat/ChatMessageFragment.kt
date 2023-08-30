package com.example.safarione.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.postOnAnimationDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safarione.databinding.FragmentChatMessageBinding
import com.example.safarione.service.ChatInterceptor
import com.example.safarione.service.ConnectionStatus
import com.example.safarione.ui.chat.adapter.ChatMessagePagingAdapter
import com.example.safarione.ui.chat.contract.ChatMessageContract
import com.example.safarione.ui.chat.contract.ChatMessageViewModel
import com.example.safarione.ui.main.contract.MainContract
import com.example.safarione.utils.collectLatestOnLifecycle
import com.example.safarione.utils.dismissKeyboard
import com.example.safarione.utils.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatMessageFragment : Fragment() {

    private val navArgs by navArgs<ChatMessageFragmentArgs>()

    private val viewModels: ChatMessageContract.ViewModel by viewModels<ChatMessageViewModel>(
        ownerProducer = { this },
        extrasProducer = { this.defaultViewModelCreationExtras },
        factoryProducer = { this.defaultViewModelProviderFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels.setRoomId(navArgs.roomId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentChatMessageBinding.inflate(inflater, container, false).apply {
        lifecycleOwner = viewLifecycleOwner
        vm = viewModels
        val adapter = ChatMessagePagingAdapter()
        recChatRoom.adapter = adapter
        viewModels.conversation.collectLatestOnLifecycle(viewLifecycleOwner) {
            val visible = withContext(Dispatchers.Main) {
                (recChatRoom.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            adapter.submitData(it)
            withContext(Dispatchers.Main) {
                if (visible <= 0 && adapter.itemCount > 0) {
                    recChatRoom.postOnAnimationDelayed(500L) {
                        recChatRoom.smoothScrollToPosition(0)
                    }
                }
            }
        }
        viewModels.room.collectLatestOnLifecycle(
            viewLifecycleOwner,
            repeatOnState = Lifecycle.State.RESUMED
        ) {
            requireActivity().title = it?.let {
                "@${it.title}"
            }
        }
        viewModels.enableSend.collectLatestOnLifecycle(viewLifecycleOwner) {
            btnSend.isClickable = it
            btnSend.isClickable = it
        }
        btnSend.setOnSafeClickListener {
            val room = viewModels.room.value ?: return@setOnSafeClickListener
            val message = viewModels.message.value
            if (message.isNullOrEmpty()) return@setOnSafeClickListener
            val interceptor = getChatInterceptorDelegate()
            if (interceptor == null || interceptor.isConnected.value != ConnectionStatus.CONNECTED) {
                Toast.makeText(
                    requireContext(),
                    "Cannot connect to chat server",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSafeClickListener
            }
            interceptor.sendToUser(room.id, message)
            viewModels.message.value = ""
        }
        executePendingBindings()
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        dismissKeyboard()
    }

    private fun getChatInterceptorDelegate(): ChatInterceptor? =
        (activity as? MainContract.ChatInterceptorDelegate)?.getChatInterceptor()?.value
}