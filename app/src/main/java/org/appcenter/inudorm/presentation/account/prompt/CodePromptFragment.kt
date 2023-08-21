package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCodePromptBinding
import org.appcenter.inudorm.presentation.LoadingFragment
import org.appcenter.inudorm.util.eventHandler


class CodePromptFragment : LoadingFragment() {

    private lateinit var viewModel: CodePromptViewModel
    private lateinit var binding: FragmentCodePromptBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_code_prompt, container, false)

        val bundle = this.arguments

        viewModel = ViewModelProvider(
            viewModelStore,
            CodePromptViewModelFactory(
                bundle?.getString("email", "none")!!,
                bundle.getSerializable("purpose") as EmailPromptPurpose
            )
        )[CodePromptViewModel::class.java]

        binding.codeViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
                viewModel.startTimer()
        lifecycleScope.launch {
            viewModel.eventFlow.collect {
                eventHandler(requireContext(), it)
            }
        }
    }
}