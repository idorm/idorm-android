package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCodePromptBinding
import org.appcenter.inudorm.databinding.FragmentCodeSentBinding
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.eventHandler

class CodeSentFragment : Fragment() {

    private lateinit var binding: FragmentCodeSentBinding
    private val TAG = "[CodeSentFragment]"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_code_sent, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.openWebMail.setOnClickListener {
            openWebMail()
        }
        binding.enterAuthCode.setOnClickListener {
            val bundle = Bundle()
            (context as OnPromptDoneListener).onPromptDone(bundle)
        }
    }

    private fun openWebMail() {
        Log.d(TAG, "trying to open webmail ...")
        CustomDialog(
            "Gmail 앱에 학교 메일이 등록되어 있으신가요?",
            DialogButton("예", {
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage("com.google.android.gm")
                if (intent == null) { // 지메일 앱 미설치. 포털로 패스
                    openWebMail()
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }),
            DialogButton("아니오", { openMailWeb() })
        ).show(this.requireContext())
    }

    private fun openMailWeb() {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://webmail.inu.ac.kr/member/login?host_domain=inu.ac.kr")
        )
        startActivity(webIntent)
    }
}