package com.example.safarione.ui.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.safarione.R
import com.example.safarione.databinding.ActivityMainBinding
import com.example.safarione.service.ChatInterceptor
import com.example.safarione.service.ChatInterceptorService
import com.example.safarione.ui.auth.AuthenticationActivity
import com.example.safarione.ui.main.contract.MainContract
import com.example.safarione.ui.main.contract.MainViewModel
import com.example.safarione.utils.collectLatestOnLifecycle
import com.example.safarione.utils.onDisposeEffect
import com.example.safarione.utils.repeatDisposableEffectOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.ChatInterceptorDelegate {

    private val uiModel by viewModels<MainUi>()

    private val viewModel: MainContract.ViewModel by viewModels<MainViewModel>(
        extrasProducer = { this.defaultViewModelCreationExtras },
        factoryProducer = { this.defaultViewModelProviderFactory }
    )

    private val stateOfChatInterceptor: MutableStateFlow<ChatInterceptor?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            setSupportActionBar(header.toolbar)
            uiModel.stateOfTitle.collectLatestOnLifecycle(this@MainActivity) {
                header.lblTitle.text = it
            }
            val navController = findNavController(R.id.nav_host_fragment)
            navController.addOnDestinationChangedListener { controller, destination, _ ->
                val isTopScreen = controller.previousBackStackEntry == null
                if (isTopScreen) {
                    header.toolbar.navigationIcon = null
                } else {
                    header.toolbar.navigationIcon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back, this@MainActivity.theme)
                }
                header.toolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
                destination.label?.let {
                    uiModel.stateOfTitle.value = it.toString()
                }
            }
        }
        repeatDisposableEffectOnLifecycle(state = Lifecycle.State.CREATED) {
            val intent = Intent(this@MainActivity, ChatInterceptorService::class.java)
            startService(intent)
            val connection = object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    stateOfChatInterceptor.value = (service as ChatInterceptorService.ChatInterceptorBinder).getInterceptor()
                    Timber.w("Chat interceptor connected")
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    stateOfChatInterceptor.value = null
                }

            }
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            onDisposeEffect {
                stateOfChatInterceptor.value = null
                unbindService(connection)
                stopService(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @SuppressLint("IntentWithNullActionLaunch")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_log_out) {
            viewModel.logOut()
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finishAffinity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setTitle(title: CharSequence?) {
        uiModel.stateOfTitle.value = title?.toString() ?: ""
    }

    override fun setTitle(titleId: Int) {
        uiModel.stateOfTitle.value = getString(titleId)
    }

    private val _chatInterceptor = stateOfChatInterceptor.asStateFlow()
    override fun getChatInterceptor(): StateFlow<ChatInterceptor?> = _chatInterceptor
}

class MainUi : ViewModel() {

    val stateOfTitle = MutableStateFlow("")
}