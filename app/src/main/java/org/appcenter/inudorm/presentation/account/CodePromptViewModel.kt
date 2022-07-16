package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.addZeroToMakeLength
import org.appcenter.inudorm.util.encrypt


class CodePromptViewModel(private val email:String, private var authorizedCode: String, private val purpose: EmailPromptPurpose) : ViewModelWithEvent() {
    private val TAG = "[CodePromptViewModel]"
    val code = MutableLiveData("")
    val userRepository = UserRepository()
    private var _timer = MutableLiveData<Int>(10) // Todo: 3분으로 수정
    private lateinit var a: Job

    val timer: LiveData<Int>
        get() = _timer

    fun startTimer() {
        if (::a.isInitialized) a.cancel()
        _timer.value = 10 // Todo: 3분으로 수정
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
        // resend code
        if (_timer.value!! > 0) {
            showDialog("인증번호가 만료되지 않았습니다.", DialogButton("확인", null, null), null)
        } else {
            viewModelScope.launch {
                kotlin.runCatching {
                    userRepository.sendAuthCode(email)
                }.onSuccess {
                    // Restart Timer
                    startTimer()
                    authorizedCode = it
                    code.value = ""
                }.onFailure {
                    // Todo: Handle resend fail
                }
            }
        }
    }

    fun submit() {
        // check code and navigate
        Log.d(TAG, "$authorizedCode | ${encrypt(code.value!!)}")
        if (_timer.value!! < 0) {
            this.showDialog("인증번호가 만료되었습니다.", DialogButton("확인", null, null), null)
        } else {
            if (authorizedCode == encrypt(code.value!!)) {
                stopTimer()
                val bundle = Bundle()
                bundle.putBoolean("authorized", true)
                this.mergeBundleWithPaging(bundle)
            } else {
                this.showDialog("인증번호를 다시 확인해주세요.", DialogButton("확인", null, null), null)
            }
        }

    }
}

class CodePromptViewModelFactory(private val email:String, private val authorizedCode: String, private val purpose: EmailPromptPurpose) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodePromptViewModel::class.java)) {
            return CodePromptViewModel(email, authorizedCode, purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}