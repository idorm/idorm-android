package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLoginBinding
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.MainActivity
import org.appcenter.inudorm.presentation.mypage.myinfo.ChangePasswordActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.OkDialog

class LoginActivity : LoadingActivity(), OnSnackBarCallListener {

    private lateinit var prefsRepository: PrefsRepository
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    // 화면 터치 시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefsRepository = PrefsRepository(applicationContext)
        viewModel = ViewModelProvider(
            viewModelStore,
            LoginViewModelFactory(prefsRepository)
        )[LoginViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                setLoadingState(state.loading)
                if (!state.loading) {
                    if (state.error == null && state.data != null) {
                        // Login Successful. Route to another page.
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (state.data == null && state.error != null) {
                        UIErrorHandler.handle(
                            this@LoginActivity,
                            PrefsRepository(this@LoginActivity),
                            state.error,
                            {
                                OkDialog(
                                    it.message ?: "알 수 없는 오류입니다.",
                                ).show(
                                    this@LoginActivity
                                )
                            }
                        ) {
                            OkDialog(
                                it.message,
                            ).show(
                                this@LoginActivity
                            )
                        }

                    }
                }
            }
        }
        binding.registerClickText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.changePasswordClickText.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
    }

    override fun onSnackBarCalled(message: String, duration: Int) {
        Snackbar.make(binding.root, message, duration)
            .apply {
                view.foregroundGravity = Gravity.BOTTOM
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .apply {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        gravity = Gravity.BOTTOM
                        foregroundGravity = Gravity.BOTTOM
                    }

            }
            .show()
    }


}