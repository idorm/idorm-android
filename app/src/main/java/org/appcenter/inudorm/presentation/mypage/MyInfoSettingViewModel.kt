package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.Member
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.GetMatchingInfo
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.usecase.SetMatchingInfoVisibility
import org.appcenter.inudorm.util.IDormLogger
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible

class MyInfoSettingViewModel : ViewModel() {
    private val _myPageState: MutableStateFlow<UiState<User>> = MutableStateFlow(UiState())
    val myPageState: StateFlow<UiState<User>>
        get() = _myPageState

    fun getUser() {
        viewModelScope.launch {
            _myPageState.update {
                it.copy(
                    loading = true
                )
            }
            runCatch(this@MyInfoSettingViewModel::_myPageState, LoginRefresh()::run, null)
        }
    }
}

data class UiState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
) {
    companion object {
        inline fun <reified T> empty() = UiState<T>(
            data = null,
            loading = true,
            error = null
        )
    }
}

suspend fun <D, P> runCatch(
    state: KProperty<MutableStateFlow<UiState<D>>>,
    runMethod: KSuspendFunction1<P, D>,
    param: P,
) {
    state.isAccessible = true
    runCatching {
        runMethod(param)
    }.onSuccess { data ->
        state.call().update {
            UiState(data = data, loading = false, error = null)
        }
    }.onFailure { e ->
        state.call().update {
            UiState(data = null, loading = false, error = e)
        }
    }
}