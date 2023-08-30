package com.example.safarione.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun LifecycleOwner.repeatOnLifecycleScope(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    launcher: suspend CoroutineScope.() -> Unit,
): Job = lifecycleScope.launch {
    repeatOnLifecycle(state, launcher)
}

fun <T> Flow<T>.collectLatestOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    repeatOnState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (T) -> Unit,
) {
    lifecycleOwner.run {
        lifecycleScope.launch {
            repeatOnLifecycle(repeatOnState) {
                collectLatest(collector)
            }
        }
    }
}

val ViewModel.useCaseContext: CoroutineContext
    get() = viewModelScope.coroutineContext + Dispatchers.IO


fun interface DisposableEffect {

    fun dispose()
}

fun onDisposeEffect(disposable: () -> Unit) = DisposableEffect(disposable)

fun LifecycleOwner.repeatDisposableEffectOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    launcher: suspend CoroutineScope.() -> DisposableEffect,
): Job = lifecycleScope.launch {
    repeatOnLifecycle(state) {
        val effect = launcher()
        when (state) {
            Lifecycle.State.CREATED -> object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    effect.dispose()
                    owner.lifecycle.removeObserver(this)
                }
            }

            Lifecycle.State.STARTED -> object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    super.onStop(owner)
                    effect.dispose()
                    owner.lifecycle.removeObserver(this)
                }
            }

            Lifecycle.State.RESUMED -> object : DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    super.onPause(owner)
                    effect.dispose()
                    owner.lifecycle.removeObserver(this)
                }
            }

            else -> null
        }?.let {
            lifecycle.addObserver(it)
        }
    }
}
