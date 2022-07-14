package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCodePromptBinding
import org.appcenter.inudorm.util.eventHandler

class CodePromptFragment : Fragment() {

    companion object {
        fun newInstance() = CodePromptFragment()
    }

    private lateinit var viewModel: CodePromptViewModel
    private lateinit var binding: FragmentCodePromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_code_prompt, container, false)
        val bundle = this.arguments
        var code : String
        if (bundle != null) {
            code = bundle.getString("encryptedCode", "none")
            Log.d("[CodePromptFragment]", "I got $code" )
        } else {
            code = "none"
        }
        viewModel = ViewModelProvider(
            viewModelStore,
            CodePromptViewModelFactory(code)
        )[CodePromptViewModel::class.java]
        binding.codeViewModel = viewModel
        binding.lifecycleOwner = this
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