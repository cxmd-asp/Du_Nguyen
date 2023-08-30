package com.example.safarione.ui.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.safarione.databinding.FragmentLoginBinding
import com.example.safarione.ui.auth.login.contract.LoginContract
import com.example.safarione.ui.auth.login.contract.LoginViewModel
import com.example.safarione.ui.main.MainActivity
import com.example.safarione.utils.collectLatestOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginContract.ViewModel by viewModels<LoginViewModel>(
        extrasProducer = { defaultViewModelCreationExtras },
        factoryProducer = { defaultViewModelProviderFactory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentLoginBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            executePendingBindings()
        }.root

    @SuppressLint("IntentWithNullActionLaunch")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resultOfLoginProcess.collectLatestOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is LoginContract.LoginResult.Success -> {
                    startActivity(
                        Intent(requireContext(), MainActivity::class.java)
                    )
                    requireActivity().finish()
                }

                is LoginContract.LoginResult.Failed -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}