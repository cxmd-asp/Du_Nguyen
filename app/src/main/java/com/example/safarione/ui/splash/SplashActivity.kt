package com.example.safarione.ui.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.example.safarione.R
import com.example.safarione.databinding.ActivitySplashBinding
import com.example.safarione.ui.auth.AuthenticationActivity
import com.example.safarione.ui.main.MainActivity
import com.example.safarione.ui.splash.contract.SplashContract
import com.example.safarione.ui.splash.contract.SplashViewModel
import com.example.safarione.utils.collectLatestOnLifecycle
import com.example.safarione.utils.repeatOnLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModels: SplashContract.ViewModel by viewModels<SplashViewModel>(extrasProducer = { defaultViewModelCreationExtras },
        factoryProducer = { defaultViewModelProviderFactory })

    @SuppressLint("IntentWithNullActionLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        viewModels.eventOfAuthenticationState.collectLatestOnLifecycle(this) {
            val destination: Class<out Activity> = if (it) MainActivity::class.java
            else AuthenticationActivity::class.java
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@SplashActivity, destination))
                finish()
            }
        }
        this.repeatOnLifecycleScope(state = Lifecycle.State.RESUMED) {
            withContext(Dispatchers.IO) {
                delay(2_000L) //splash time
            }
            viewModels.checkAuthenticationState()
        }
    }
}