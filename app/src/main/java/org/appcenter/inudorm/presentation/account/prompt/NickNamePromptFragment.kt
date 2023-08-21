package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentNicknamePromptBinding
import org.appcenter.inudorm.presentation.LoadingFragment

class NickNamePromptFragment : LoadingFragment() {
    private val viewModel: NickNamePromptViewModel by viewModels()
    private lateinit var binding: FragmentNicknamePromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_nickname_prompt, container, false)
        binding.nickNamePromptViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
                binding.continueButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", viewModel.nickName.value)
            (this@NickNamePromptFragment.requireContext() as OnPromptDoneListener).onPromptDone(
                bundle
            )
        }
    }

}