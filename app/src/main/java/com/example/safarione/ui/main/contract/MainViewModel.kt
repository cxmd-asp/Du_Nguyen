package com.example.safarione.ui.main.contract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.safarione.module.credential.CredentialManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val credentialManager: CredentialManager
) : AndroidViewModel(application), MainContract.ViewModel {

    override fun logOut() {
        viewModelScope.launch {
            credentialManager.clearCredential()
        }
    }
}