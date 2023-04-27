package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.usecase.SendAuthCode
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.State
import org.appcenter.inudorm.util.ViewModelWithEvent
import org.appcenter.inudorm.util.emailValidator


class EmailPromptViewModel(private val purpose: EmailPromptPurpose) : ViewModelWithEvent() {
    val email = MutableLiveData("")
    private val _emailResultState: MutableStateFlow<State<Boolean>> =
        MutableStateFlow(State.Initial())
    val emailResultState: StateFlow<State<Boolean>>
        get() = _emailResultState

    fun submit() {
        val mail = email.value!!
        if (emailValidator(mail)) { // 올바른 메일인지 체크좀 할게요..
            viewModelScope.launch {
                _emailResultState.update {
                    State.Loading()
                }
                kotlin.runCatching {
                    SendAuthCode().run(SendAuthCodeParams(purpose, email.value!!))
                }.onSuccess {
                    val bundle = Bundle()
                    bundle.putString("email", mail)
                    bundle.putSerializable("purpose", purpose)
                    mergeBundleWithPaging(bundle)
                    _emailResultState.update {
                        State.Success(true)
                    }
                }.onFailure { e ->
                    // MEMBER_NOT_FOUND 만 예외처리, 추후 새로운 아키텍처로 이전
                    if (e is IDormError) {
                        when (e.error) {
                            ErrorCode.MEMBER_NOT_FOUND -> {
                                showToast("존재하지 않는 회원입니다.")
                                _emailResultState.update {
                                    State.Error(e)
                                }
                                return@launch
                            }
                            else -> {}
                        }

                    }
                    showToast(e.message ?: "알 수 없는 오류입니다.")
                    _emailResultState.update {
                        State.Error(e)
                    }
                }
            }
        } else {
            showDialog("이메일 형식을 확인해 주세요.", DialogButton("확인"))
        }
    }
}

class EmailPromptViewModelFactory(private val purpose: EmailPromptPurpose) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailPromptViewModel::class.java)) {
            return EmailPromptViewModel(purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}