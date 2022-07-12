package org.appcenter.inudorm.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLoginBinding
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
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
    }


}