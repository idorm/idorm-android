package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLoginBinding
import org.appcenter.inudorm.presentation.MainActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

class LoginActivity : AppCompatActivity() {

    private lateinit var prefsRepository: PrefsRepository
    private lateinit var viewModel : LoginViewModel
    private lateinit var binding: ActivityLoginBinding

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
                if (state.success) {
                    // Login Successful. Route to another page.
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (state.message != null) {
                    CustomDialog(
                        state.message!!,
                        DialogButton("확인", null)
                    ).show(this@LoginActivity)
                }
            }
        }
        binding.registerClickText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


}