package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.CheckIfCodeCorrect
import org.appcenter.inudorm.usecase.CodeVerifyParams
import org.appcenter.inudorm.usecase.SendAuthCode
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.addZeroToMakeLength
import org.appcenter.inudorm.util.encrypt

private const val initialTime = 60 * 3
/**
 * 사용된 UseCase
 * 1. 이메일과 메일 전송 목적을 받아 메일을 재전송할 수 있습니다.
 * 2. 인증번호 검증 요청을 서버에 보낼 수 있습니다.
 */
class CodePromptViewModel(private val email: String, private val purpose: EmailPromptPurpose) : ViewModelWithEvent() {
    val code = MutableLiveData("")
    val userRepository = UserRepository()
    private var _timer = MutableLiveData(initialTime) // Todo: 3분으로 수정
    private lateinit var a: Job

    val timer: LiveData<Int>
        get() = _timer

    fun startTimer() {
        if (::a.isInitialized) a.cancel()
        _timer.value = initialTime // Todo: 3분으로 수정
        a = viewModelScope.launch {
            while (_timer.value!! > 0) {
                _timer.value = _timer.value!!.minus(1)
                delay(1000L)
            }
        }
    }

    private fun stopTimer() {
        if (::a.isInitialized) a.cancel()
    }

    fun resendCode() {
        if (_timer.value!! > 0) {
            showDialog("인증번호가 만료되지 않았습니다.", DialogButton("확인"))
        } else {
            viewModelScope.launch {
                kotlin.runCatching {
                    SendAuthCode().run(SendAuthCodeParams(purpose, email))
                }.onSuccess {
                    // Restart Timer
                    startTimer()
                    code.value = ""
                }.onFailure {
                    // Todo: Handle resend fail
                    showDialog("이메일 재전송에 실패했습니다.", DialogButton("확인"))
                }
            }
        }
    }

    fun submit() {
        if (_timer.value!! < 0) {
            this.showDialog("인증번호가 만료되었습니다.", DialogButton("확인"))
        } else {
            // check if code correct on server
            viewModelScope.launch {
                kotlin.runCatching {
                    CheckIfCodeCorrect().run(CodeVerifyParams(purpose, email, code.value!!))
                }.onSuccess {
                    stopTimer()
                    val bundle = Bundle()
                    bundle.putBoolean("authorized", true)
                    bundle.putString("authCode", code.value!!)
                    this@CodePromptViewModel.mergeBundleWithPaging(bundle)
                }.onFailure {
                    this@CodePromptViewModel.showDialog(
                        "인증번호를 다시 확인해주세요.", DialogButton("확인")
                    )
                }
            }
        }

    }
}

class CodePromptViewModelFactory(
    private val email: String,
    private val purpose: EmailPromptPurpose
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodePromptViewModel::class.java)) {
            return CodePromptViewModel(email, purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}