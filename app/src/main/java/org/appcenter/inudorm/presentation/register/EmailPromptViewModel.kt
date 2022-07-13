package org.appcenter.inudorm.presentation.register

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.emailValidator


class EmailPromptViewModel : ViewModelWithEvent() {
    val email = MutableLiveData("")
    private val userRepository = UserRepository()

    fun showNotValidDialog() {
        val okButton = DialogButton("확인", Color.CYAN) {
            showToast("Toast Message")
        }
        showDialog("이메일이 올바르지 않습니다.", okButton, null)
    }

    fun submit() {
        val mail = email.value!!
        if (emailValidator(mail)) {
            viewModelScope.launch {
                kotlin.runCatching {
                    userRepository.sendAuthCode(mail)
                }.onSuccess {
                    val bundle = Bundle()
                    bundle.putString("email", mail)
                    bundle.putString("encryptedCode", it)
                    mergeBundleWithPaging(bundle)
                }.onFailure {
                    showToast("이메일 전송에 실패한 것 같습니다.")
                }
            }
        } else {
            showDialog("이메일이 올바르지 않습니다.", DialogButton("확인", null, null), null)
        }
    }
}

