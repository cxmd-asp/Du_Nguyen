package com.example.safarione.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safarione.R
import com.example.safarione.databinding.FragmentChatSingleCreateBinding
import com.example.safarione.ui.member.adapter.ChatMemberAdapter
import com.example.safarione.ui.member.contract.CreateSingleChatContract
import com.example.safarione.ui.member.contract.CreateSingleChatViewModel
import com.example.safarione.utils.collectLatestOnLifecycle
import com.example.safarione.utils.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateSingleChatFragment : Fragment() {

    private val viewModel: CreateSingleChatContract.ViewModel by viewModels<CreateSingleChatViewModel>(
        ownerProducer = { this },
        extrasProducer = { this.defaultViewModelCreationExtras },
        factoryProducer = { this.defaultViewModelProviderFactory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentChatSingleCreateBinding.inflate(inflater, container, false).apply {
        lifecycleOwner = viewLifecycleOwner
        vm = viewModel
        val adapter = ChatMemberAdapter { _, _, model ->
            model ?: return@ChatMemberAdapter
            viewModel.requestCreateChat(model)
        }
        recChatRoom.adapter = adapter
        viewModel.members.collectLatestOnLifecycle(viewLifecycleOwner) {
            adapter.submitData(it)
        }
        viewModel.eventOfSingleChatCreated.collectLatestOnLifecycle(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_common_error),
                    Toast.LENGTH_SHORT
                ).show()
                return@collectLatestOnLifecycle
            }
            findNavController().navigate(
                CreateSingleChatFragmentDirections.toChatMessage(it.id)
            )
        }
        executePendingBindings()
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        dismissKeyboard()
    }
}