package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.merge
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.ViewModelWithEvent


class PasswordPromptViewModel : ViewModelWithEvent() {
    private val TAG = "[PasswordPromptViewModel]"
    val password = MutableLiveData("")
    val userRepository = UserRepository()

    fun submit() {
        val bundle = Bundle()
        bundle.putString("password", password.value!!)
        mergeBundleWithPaging(bundle)
    }

}
