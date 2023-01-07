package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityRegisterBinding
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.account.*
import org.appcenter.inudorm.presentation.account.prompt.*
import org.appcenter.inudorm.usecase.Register
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger

class RegisterActivity : PromptActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    private var registerBundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages(): ArrayList<Fragment> {
        val initPage = ArrayList<Fragment>()
        val fragment = EmailPromptFragment()
        val bundle = Bundle()
        bundle.putSerializable("purpose", EmailPromptPurpose.Register)
        fragment.arguments = bundle
        initPage.add(fragment)
        return initPage
    }

    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        registerBundle.putAll(data)
        if (binding.pager.currentItem == pagerAdapter.itemCount - 1)
            when (binding.pager.currentItem) {
                0 -> {
                    Toast.makeText(this, "메일 전송 안내 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    addPage(CodeSentFragment())
                }
                1 -> {
                    Toast.makeText(this, "암호화된 코드를 갖고 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    val fragment = CodePromptFragment()
                    fragment.arguments = registerBundle
                    addPage(fragment)
                }
                2 -> {
                    Toast.makeText(this, "비밀번호 설정 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    addPage(PasswordPromptFragment())
                }
                3 -> {
                    Toast.makeText(this, "비밀번호 설정 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    addPage(NickNamePromptFragment())
                }
                4 -> {
                    Toast.makeText(this, "회원가입 시도", Toast.LENGTH_SHORT).show()
                    val email = registerBundle.getString("email", "none")
                    val password = registerBundle.getString("password", "none")
                    _register(email, password)
                }
            }
        binding.pager.currentItem++
        setToolbarIcon()
    }

    private fun _register(email: String, password: String) {
        lifecycleScope.launch {
            kotlin.runCatching {
                Register().run(RegisterParams(email, password))
            }.onSuccess { result ->
                IDormLogger.i(this, result.toString())
                if (result) {
                    // Register completed, change page
                    val intent =
                        Intent(this@RegisterActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.onError {
                // 네트워크 관련 에러만 대응 피룡.
                IDormLogger.e(this@RegisterActivity, it.toString())
                CustomDialog(
                    it.toString(),
                    positiveButton = DialogButton("확인")
                ).show(this@RegisterActivity)
            }.onExpectedError {
                CustomDialog(
                    it.message,
                    positiveButton = DialogButton("확인")
                ).show(this@RegisterActivity)
            }
        }
    }

}