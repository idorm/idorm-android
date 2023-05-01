package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.usecase.GetMatchingInfo
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.usecase.SetMatchingInfoVisibility

data class MyPageState(
    val myInfo: Result<User?>?,
    val matchingInfo: Result<MatchingInfo?>?,
    val matchingInfoToggleState: Result<Boolean>?,
)

data class Result<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
) {
    fun isError() = !loading && error != null
    fun isSuccess() = !loading && error == null
    fun isLoading() = loading
}

sealed class UserMutationResult() {
    data class ToggleMatchingInfoVisibility(
        val success: Boolean? = null,
        val loading: Boolean = false,
        val error: Throwable? = null
    ) :
        UserMutationResult()
}

class MyPageViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _myPageState = MutableStateFlow(MyPageState(null, null, null))
    val myPageState: StateFlow<MyPageState>
        get() = _myPageState

    private val _myPageMutation: MutableStateFlow<UserMutationResult?> = MutableStateFlow(null)
    val myPageMutation: StateFlow<UserMutationResult?>
        get() = _myPageMutation


    fun toggleMatchingInfoVisibility() {
        viewModelScope.launch {
            _myPageMutation.emit(
                UserMutationResult.ToggleMatchingInfoVisibility(
                    loading = true,
                )
            )
            kotlin.runCatching {
                SetMatchingInfoVisibility().run(!myPageState.value.matchingInfo?.data?.isMatchingInfoPublic!!)
            }.onSuccess {
                _myPageMutation.emit(
                    UserMutationResult.ToggleMatchingInfoVisibility(
                        success = true,
                        loading = false,
                    )
                )
            }.onFailure { err ->
                _myPageMutation.emit(
                    UserMutationResult.ToggleMatchingInfoVisibility(
                        success = false,
                        loading = false,
                        error = err
                    )
                )
            }
        }
    }

    // Todo: 개똥코드입니다!!!!! ㄹ무조건 리팩하세요
    fun getMyMatchingInfo() {
        viewModelScope.launch {
            _myPageState.update {
                it.copy(
                    myInfo = Result(loading = true),
                    matchingInfo = Result(loading = true),
                    matchingInfoToggleState = Result(loading = true),
                )
            }
            kotlin.runCatching {
                LoginRefresh().run(null)
            }.onSuccess { user ->
                kotlin.runCatching {
                    GetMatchingInfo().run(null)
                }.onSuccess { info ->
                    _myPageState.update {
                        it.copy(
                            myInfo = Result(data = user, loading = false),
                            matchingInfo = Result(data = info, loading = false)
                        )
                    }
                }.onFailure { e ->
                    _myPageState.update {
                        it.copy(
                            myInfo = Result(data = user, loading = false, error = e),
                            matchingInfo = Result(data = null, loading = false, error = e)
                        )
                    }
                }
            }.onFailure { e ->
                _myPageState.update {
                    it.copy(
                        myInfo = Result(loading = false, error = e),
                        matchingInfo = Result(loading = false, error = e)
                    )
                }
            }
        }
    }
}