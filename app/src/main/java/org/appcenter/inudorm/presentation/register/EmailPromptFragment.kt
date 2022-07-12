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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentEmailPromptBinding
import org.appcenter.inudorm.util.Event

class EmailPromptFragment : Fragment() {

    companion object {
        fun newInstance() = EmailPromptFragment()
    }

    private val viewModel: EmailPromptViewModel by viewModels()
    private lateinit var binding: FragmentEmailPromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_prompt, container, false)
        binding.emailViewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.eventFlow.collect {
                Log.d("Fragment", it.toString())
                when (it) {
                    is Event.ShowToast -> {
                        Toast.makeText(context, it.text, it.interval).show()
                    }
                    is Event.ShowDialog -> {
                        val dialog = AlertDialog.Builder(context)
                            .setMessage(it.text)
                        if (it.positiveButton != null) {
                            dialog.setPositiveButton(
                                it.positiveButton.text,
                                (DialogInterface.OnClickListener { _, _ -> it.positiveButton.onClick() })
                            )
                        }
                        if (it.negativeButton != null) {
                            dialog.setNegativeButton(
                                it.negativeButton.text,
                                (DialogInterface.OnClickListener { _, _ -> it.negativeButton.onClick() })
                            )
                        }
                        dialog.create().show()
                    }
                    is Event.MergeBundleWithPaging<*> -> {
                        val bundle = Bundle()
                        bundle.putString(it.key, it.data as String)
                        (context as OnPromptDoneListener).onPromptDone(bundle)
                    }
                }
            }
        }
    }
}