package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentPasswordPromptBinding
import org.appcenter.inudorm.presentation.LoadingFragment
import org.appcenter.inudorm.util.eventHandler

class PasswordPromptFragment : LoadingFragment() {

    private val viewModel: PasswordPromptViewModel by viewModels()
    private lateinit var binding: FragmentPasswordPromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_password_prompt, container, false)
        binding.passwordViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLoadingState(false)
        lifecycleScope.launch {
            viewModel.eventFlow.collect {
                eventHandler(requireContext(), it)
            }
        }
        binding.passwordField.addTextChangedListener {
            viewModel.setPasswordState(PasswordChange(it.toString()))
        }
        binding.passwordCheckField.addTextChangedListener {
            viewModel.setPasswordState(PasswordCheckChange(it.toString()))
        }
    }
}