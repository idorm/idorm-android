package org.appcenter.inudorm.presentation.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.usecase.UserInputParams
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.emailValidator
import org.appcenter.inudorm.util.passwordValidator
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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

class LoginViewModel(private val prefsRepository: PrefsRepository) : ViewModel() {
    private val TAG = "[LoginViewModel]"

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    private val _loginState = MutableStateFlow(UiState<Boolean>(null))
    val loginState: StateFlow<UiState<Boolean>>
        get() = _loginState


    fun tryToLoginWithInput() {
        // 이메일과 비밀번호가 입력 조건을 만족하면
        if (emailValidator(email.value!!) && passwordValidator(password.value!!)) {
            IDormLogger.i(this, "입력조건충족")
            viewModelScope.launch {
                _loginState.emit(
                    Login(prefsRepository).run(
                        UserInputParams(
                            email.value!!,
                            password.value!!
                        )
                    )
                )
            }
        } else {
            viewModelScope.launch {
                _loginState.emit(
                    UiState(error = java.lang.RuntimeException("아이디나 비밀번호가 올바르지 않아요."))
                )
            }
        }

    }
}

class LoginViewModelFactory(private val prefsRepository: PrefsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}