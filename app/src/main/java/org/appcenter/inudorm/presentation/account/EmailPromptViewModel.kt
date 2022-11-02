package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.SendAuthCode
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.emailValidator


class EmailPromptViewModel(private val purpose: EmailPromptPurpose) : ViewModelWithEvent() {
    val email = MutableLiveData("")

    fun submit() {
        val mail = email.value!!
        if (emailValidator(mail)) { // 올바른 메일인지 체크좀 할게요..
            viewModelScope.launch {
                kotlin.runCatching {
                    SendAuthCode().run(SendAuthCodeParams(purpose, email.value!!))
                }.onSuccess {
                    val bundle = Bundle()
                    bundle.putString("email", mail)
                    bundle.putSerializable("purpose", purpose)
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

class EmailPromptViewModelFactory(private val purpose: EmailPromptPurpose) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailPromptViewModel::class.java)) {
            return EmailPromptViewModel(purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}