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


class PasswordPromptViewModel() : ViewModelWithEvent() {
    private val TAG = "[PasswordPromptViewModel]"
    val code = MutableLiveData("")
    val userRepository = UserRepository()

    fun resendCode() {
        // resend code
    }

    fun submit() {
        // check code and navigate

    }
}
