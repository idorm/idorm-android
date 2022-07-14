package org.appcenter.inudorm.presentation.register

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentEmailPromptBinding
import org.appcenter.inudorm.presentation.account.EmailPromptViewModel
import org.appcenter.inudorm.presentation.account.EmailPromptViewModelFactory
import org.appcenter.inudorm.util.Event
import org.appcenter.inudorm.util.eventHandler

enum class EmailPromptPurpose  {
    Register,
    FindPass
}

class EmailPromptFragment : Fragment() {

    companion object {
        fun newInstance() = EmailPromptFragment()
    }

    private lateinit var viewModel: EmailPromptViewModel

    private lateinit var binding: FragmentEmailPromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_prompt, container, false)
        val bundle = this.arguments
        val purpose : EmailPromptPurpose
        if (bundle != null) {
            purpose = bundle.getSerializable("purpose") as EmailPromptPurpose
            Log.d("[EmilPromptFragment]", "I got $purpose" )
        } else {
            purpose = EmailPromptPurpose.Register
        }
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