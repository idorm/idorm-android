package org.appcenter.inudorm.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFindPassBinding
import org.appcenter.inudorm.model.ChangePasswordDto
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.prompt.CodePromptFragment
import org.appcenter.inudorm.presentation.account.prompt.EmailPromptFragment
import org.appcenter.inudorm.presentation.account.prompt.EmailPromptPurpose
import org.appcenter.inudorm.presentation.account.prompt.PasswordPromptFragment
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.ChangePassword
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

/**
 * 비밀번호 찾기를 재활용해 비밀번호 입력 프롬포트만 띄웁니당
 */
class ChangePasswordActivity : PromptActivity() {

    private val binding: ActivityFindPassBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_find_pass)
    }

    private var changePassBundle: Bundle = Bundle()
    private val prefsRepository: PrefsRepository by lazy {
        PrefsRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        binding.toolbarText.text = "비밀번호 변경"
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages(): ArrayList<Fragment> {
        val initPage = ArrayList<Fragment>()
        val fragment = EmailPromptFragment()
        val bundle = Bundle()
        bundle.putSerializable("purpose", EmailPromptPurpose.FindPass)
        fragment.arguments = bundle
        initPage.add(fragment)
        return initPage
    }


    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        changePassBundle.putAll(data)
        if (binding.pager.currentItem == pagerAdapter.itemCount - 1)
            when (binding.pager.currentItem) {
                0 -> {
                    Toast.makeText(this, "암호화된 코드를 갖고 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    val fragment = CodePromptFragment()
                    fragment.arguments = data
                    addPage(fragment)
                }
                1 -> {
                    Toast.makeText(this, "비밀번호 설정 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    val fragment = PasswordPromptFragment()
                    fragment.arguments = data
                    addPage(fragment)
                }
                2 -> {
                    Toast.makeText(this, "비밀번호 변경 시도", Toast.LENGTH_SHORT).show()
                    val password = changePassBundle.getString("password")
                    lifecycleScope.launch {
                        kotlin.runCatching {
                            ChangePassword().run(ChangePasswordDto(password))
                        }.onSuccess {
                            CustomDialog(
                                getString(R.string.changePasswordSuccess),
                                positiveButton = DialogButton("확인", onClick = {
                                    lifecycleScope.launch {
                                        prefsRepository.signOut()
                                    }
                                    startActivity(
                                        Intent(
                                            this@ChangePasswordActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                })
                            ).show(this@ChangePasswordActivity)
                        }.onFailure {
                            CustomDialog(
                                getString(R.string.changePasswordFailure),
                                positiveButton = DialogButton("확인")
                            ).show(this@ChangePasswordActivity)
                        }
                    }
                }
            }
        binding.pager.currentItem++
        setToolbarIcon()
    }
}