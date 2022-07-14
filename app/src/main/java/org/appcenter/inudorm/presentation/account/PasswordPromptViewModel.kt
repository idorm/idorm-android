package org.appcenter.inudorm.presentation.account

import androidx.lifecycle.MutableLiveData
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.ViewModelWithEvent


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
