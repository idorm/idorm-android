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

    private lateinit var viewModel: CodePromptViewModel
    private lateinit var binding: FragmentCodePromptBinding

    private fun getCodeFromBundle() : String { // 야생의 번들을 잡았다! 코드내놔!
        val bundle = this.arguments
        return if (bundle != null) {
            bundle.getString("encryptedCode", "none")
        } else {
            "none"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_code_prompt, container, false)

        val code = getCodeFromBundle()
        Log.d("[CodePromptFragment]", "I got $code" )

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