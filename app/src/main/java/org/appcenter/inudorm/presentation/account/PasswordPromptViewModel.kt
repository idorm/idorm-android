package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.passwordCharacterValidator
import org.appcenter.inudorm.util.passwordValidator

data class PasswordState(
    var password: String,
    var passwordCheck: String,
    var pwCharValid: Boolean? = null,
    var pwLenValid: Boolean? = null,
    var pwMatches: Boolean? = null,
    var tried: Boolean = false
)

sealed class PasswordEvent(open val value: String)
data class PasswordChange(override val value: String) : PasswordEvent(value)
data class PasswordCheckChange(override val value: String) : PasswordEvent(value)

class PasswordPromptViewModel : ViewModelWithEvent() {
    private val TAG = "[PasswordPromptViewModel]"
    var passwordState = MutableStateFlow(PasswordState("", ""))

    fun setPasswordState(event: PasswordEvent) {
        passwordState.update {
            when (event) {
                is PasswordChange -> getPasswordValidatedState(it.copy(password = event.value))
                is PasswordCheckChange -> getPasswordValidatedState(it.copy(passwordCheck = event.value))
            }
        }
    }

    private fun getPasswordValidatedState(state: PasswordState): PasswordState {
        return state.copy(
            pwCharValid = passwordCharacterValidator(state.password),
            pwLenValid = state.password.length in 8..15,
            pwMatches = state.password == state.passwordCheck
        )
    }

    fun submit() {
        val bundle = Bundle()
        if (passwordValidator(passwordState.value.password) && passwordState.value.password == passwordState.value.passwordCheck) {
            passwordState.update {
                it.copy(tried = true)
            }
            bundle.putString("password", passwordState.value.password)
            mergeBundleWithPaging(bundle)
        }
    }

}
