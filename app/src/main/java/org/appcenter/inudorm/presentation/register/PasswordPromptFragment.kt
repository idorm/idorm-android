package org.appcenter.inudorm.presentation.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCodePromptBinding
import org.appcenter.inudorm.databinding.FragmentPasswordPromptBinding
import org.appcenter.inudorm.util.eventHandler

class PasswordPromptFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordPromptFragment()
    }

    private val viewModel: PasswordPromptViewModel by viewModels()
    private lateinit var binding: FragmentPasswordPromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_password_prompt, container, false)
        binding.passwordViewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.eventFlow.collect {
                eventHandler(requireContext(), it)
            }
        }
    }
}