package org.appcenter.inudorm.presentation.register

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.appcenter.inudorm.presentation.LoginViewModel
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.encrypt


class CodePromptViewModel(private val authorizedCode: String) : ViewModelWithEvent() {
    private val TAG = "[CodePromptViewModel]"
    val code = MutableLiveData("")
    val userRepository = UserRepository()

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