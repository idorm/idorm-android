package org.appcenter.inudorm.presentation.account.prompt

import CheckableItem
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
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentNicknamePromptBinding
import org.appcenter.inudorm.databinding.FragmentPasswordPromptBinding
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.eventHandler

class NickNamePromptFragment : Fragment() {
    private val viewModel: NickNamePromptViewModel by viewModels()
    private lateinit var binding: FragmentNicknamePromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_nickname_prompt, container, false)
        binding.nickNamePromptViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.continueButton.setOnClickListener {
            // Todo: Open Agreement BottomSheet
            val modalBottomSheet = AgreementBottomSheetFragment(
                arrayListOf(
                    CheckableItem(
                        id="term",
                        text = "회원가입 약관 필수동의",
                        required = true,
                        checked = false,
                        url="https://google.com"
                    ),
                    CheckableItem(
                        id="privacyPolicy",
                        text = "개인정보 처리방침 필수동의",
                        required = true,
                        checked = false,
                        url="https://naver.com"

                    ),
                )
            ) {
                onAgreementAccepted()
            }
            modalBottomSheet.show(
                requireActivity().supportFragmentManager,
                ListBottomSheet.TAG
            )
        }
    }

    private fun onAgreementAccepted() {
        val agreed = true
        val nickName = viewModel.nickName.value!!
        if (nickName.matches("^[A-Za-zㄱ-ㅎ가-힣0-9]{2,8}$".toRegex())) {
            if (agreed) {
                val bundle = Bundle()
                bundle.putString("nickname", nickName)
                (this@NickNamePromptFragment.requireContext() as OnPromptDoneListener).onPromptDone(bundle)
            } else {
                CustomDialog("약관에 동의해야 합니다.", positiveButton = DialogButton("확인")).show(
                    this@NickNamePromptFragment.requireContext()
                )
            }
        } else CustomDialog(
            "닉네임 형식이 올바르지 않습니다.",
            positiveButton = DialogButton("확인")
        ).show(this@NickNamePromptFragment.requireContext())
    }

}