package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentEmailPromptBinding
import org.appcenter.inudorm.util.eventHandler

enum class EmailPromptPurpose  {
    Register,
    FindPass
}

class EmailPromptFragment : Fragment() {

    private lateinit var viewModel: EmailPromptViewModel

    private lateinit var binding: FragmentEmailPromptBinding

    private fun getPurposeFromBundle() : EmailPromptPurpose { // Fragment가 전달받은 Bundle을 풀어해쳐 이메일 입력을 받는 목적을 가져와요..
        val bundle = this.arguments
        return if (bundle != null) {
            bundle.getSerializable("purpose") as EmailPromptPurpose
        } else {
            EmailPromptPurpose.Register
        }
    }

    private fun setView(purpose: EmailPromptPurpose) {
        if (purpose == EmailPromptPurpose.FindPass) {
            binding.emailText.text = getString(R.string.needEmailText)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val purpose = getPurposeFromBundle()
        Log.d("[EmilPromptFragment]", "I got $purpose" )

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_prompt, container, false)
        setView(purpose)
        viewModel = ViewModelProvider(
            viewModelStore,
            EmailPromptViewModelFactory(purpose)
        )[EmailPromptViewModel::class.java]
        binding.emailViewModel = viewModel
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