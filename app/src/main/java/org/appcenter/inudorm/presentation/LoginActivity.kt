package org.appcenter.inudorm.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLoginBinding
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity() {

    private val TAG = "[LoginActivity]"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(dataStore)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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