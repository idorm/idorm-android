package org.appcenter.inudorm.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.usecase.UserInputParams
import org.appcenter.inudorm.util.emailValidator
import org.appcenter.inudorm.util.passwordValidator

class LoginState(var success: Boolean, var message: String?)

class LoginViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    private val TAG = "[LoginViewModel]"

    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    private val _loginState = MutableStateFlow(LoginState(false, null))
    val loginState : StateFlow<LoginState>
        get() = _loginState

    fun tryToLoginWithInput() {
        if (emailValidator(email.value!!) && passwordValidator(password.value!!)) // 이메일과 비밀번호가 입력 조건을 만족하면
            Log.d(TAG, "입력조건충족")
            viewModelScope.launch {
                kotlin.runCatching {
                    Login(dataStore).run(UserInputParams(email.value!!, password.value!!))
                }.onSuccess {
                    Log.d(TAG, "로긘 성공")
                    _loginState.emit(LoginState(true, "로그인 성공"))
                }.onFailure {
                    Log.d(TAG, "Login Failed: ${it.message}")
                    _loginState.emit(LoginState(false, it.message!!))
                }
            }
    }
}

class LoginViewModelFactory(private val dataStore: DataStore<Preferences>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}