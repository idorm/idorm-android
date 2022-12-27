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
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.usecase.UserInputParams
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.emailValidator
import org.appcenter.inudorm.util.passwordValidator
import java.net.UnknownHostException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KFunction1

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onError(
    action: (Throwable) -> Unit,
): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let {
        if (it !is IDormError)
            action(it)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onExpectedError(
    action: (IDormError) -> Unit,
): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let {
        if (it is IDormError)
            action(it)
    }
    return this
}


class LoginState(var success: Boolean, var message: String?)

class LoginViewModel(private val prefsRepository: PrefsRepository) : ViewModel() {
    private val TAG = "[LoginViewModel]"

    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    private val _loginState = MutableStateFlow(LoginState(false, null))
    val loginState: StateFlow<LoginState>
        get() = _loginState


    fun tryToLoginWithInput() {
        // 이메일과 비밀번호가 입력 조건을 만족하면
        if (emailValidator(email.value!!) && passwordValidator(password.value!!)) {
            IDormLogger.i(this, "입력조건충족")
            viewModelScope.launch {
                kotlin.runCatching {
                    Login(prefsRepository).run(UserInputParams(email.value!!, password.value!!))
                }.onSuccess {
                    IDormLogger.i(this, "로긘 성공!")
                    _loginState.emit(LoginState(true, "로그인 성공"))
                }.onError {
                    _loginState.emit(
                        LoginState(
                            false,
                            message = it.message
                        )
                    )
                    IDormLogger.i(this, "로그인 실패,,,")
                }.onExpectedError {
                    _loginState.emit(
                        LoginState(
                            false,
                            message = it.message
                        )
                    )
                }
            }
        }

    }
}

class LoginViewModelFactory(private val prefsRepository: PrefsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}