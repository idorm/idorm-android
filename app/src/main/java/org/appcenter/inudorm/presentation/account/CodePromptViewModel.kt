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


class CodePromptViewModel(private val authorizedCode: String) : ViewModelWithEvent() {
    private val TAG = "[CodePromptViewModel]"
    val code = MutableLiveData("")
    val userRepository = UserRepository()
    private val _timer = MutableLiveData<String>()
    private var _seconds = 3 * 60
    private lateinit var a: Job

    val timer: LiveData<String>
        get() = _timer
    
    fun startTimer() {
        if (::a.isInitialized) a.cancel()
        _seconds = 3 * 60
        a = viewModelScope.launch { 
            while( _seconds > 0) {
                Log.d(TAG, "$_seconds | ${timer.value}")
                _seconds --
                val min = addZeroToMakeLength(_seconds / 60, 2)
                val sec = addZeroToMakeLength(_seconds % 60, 2)
                _timer.value = "$min:$sec"
                delay(1000L)
            }
        }
    }

    fun stopTimer() {
        if(::a.isInitialized) a.cancel()
    }
    

    fun resendCode() {
        // resend code
    }

    fun submit() {
        // check code and navigate
        Log.d(TAG, "$authorizedCode | ${encrypt(code.value!!)}")
        if (authorizedCode == encrypt(code.value!!)) {
            val bundle = Bundle()
            bundle.putBoolean("authorized", true)
            this.mergeBundleWithPaging(bundle)
        } else {
            this.showDialog("인증번호를 다시 확인해주세요.", DialogButton("확인", null, null), null)
        }
    }
}

class CodePromptViewModelFactory(private val authorizedCode: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodePromptViewModel::class.java)) {
            return CodePromptViewModel(authorizedCode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}