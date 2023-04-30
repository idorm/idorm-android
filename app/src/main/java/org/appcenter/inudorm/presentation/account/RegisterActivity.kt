package org.appcenter.inudorm.presentation.account

import CheckableItem
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityRegisterBinding
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.PromptActivity
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
        // 특정 페이지 테스트용입니다.
//        addPage(NickNamePromptFragment())
//        binding.pager.currentItem++
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
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
                    addPage(CodeSentFragment())
                }
                1 -> {
                    val fragment = CodePromptFragment()
                    fragment.arguments = registerBundle
                    addPage(fragment)
                }
                2 -> {
                    addPage(PasswordPromptFragment())
                }
                3 -> {
                    addPage(NickNamePromptFragment())
                }
                4 -> {
                    val modalBottomSheet = AgreementBottomSheetFragment(
                        arrayListOf(
                            CheckableItem(
                                id = "privacyPolicy",
                                text = getString(R.string.privacyPolicyAgreement),
                                required = true,
                                checked = false,
                                url = "https://idorm.notion.site/e5a42262cf6b4665b99bce865f08319b"
                            ),
                        )
                    ) {
                        onAgreementAccepted()
                    }
                    modalBottomSheet.show(
                        this@RegisterActivity.supportFragmentManager,
                        ListBottomSheet.TAG
                    )
                }
            }
        binding.pager.currentItem++
        setToolbarIcon()
    }


    private fun onAgreementAccepted() {
        val email = registerBundle.getString("email", "none")
        val password = registerBundle.getString("password", "none")
        val nickname = registerBundle.getString("nickname", "none")

        val agreed = true
        if (nickname.matches("^[A-Za-zㄱ-ㅎ가-힣0-9]{2,8}$".toRegex())) {
            if (agreed) {
                _register(email, password, nickname)
            } else {
                CustomDialog("약관에 동의해야 합니다.", positiveButton = DialogButton("확인")).show(
                    this@RegisterActivity
                )
            }
        } else CustomDialog(
            "닉네임 형식이 올바르지 않습니다.",
            positiveButton = DialogButton("확인")
        ).show(this@RegisterActivity)
    }

    private fun _register(email: String, password: String, nickname: String) {
        lifecycleScope.launch {
            kotlin.runCatching {
                Register().run(RegisterParams(email, password, nickname))
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