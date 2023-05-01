package org.appcenter.inudorm.presentation.mypage.myinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Photo
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.presentation.matching.MyInfoMutationEvent
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.usecase.UpdateProfilePhoto
import org.appcenter.inudorm.util.State
import kotlin.reflect.KProperty
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.jvm.isAccessible


class MyInfoSettingViewModel : ViewModel() {
    private val _myPageState: MutableStateFlow<UiState<User>> = MutableStateFlow(UiState())
    val myPageState: StateFlow<UiState<User>>
        get() = _myPageState

    private val _myInfoMutationEvent: MutableStateFlow<MyInfoMutationEvent?> =
        MutableStateFlow(null)
    val myInfoMutationEvent: StateFlow<MyInfoMutationEvent?>
        get() = _myInfoMutationEvent


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

    fun updateProfilePhoto(photo: Photo) {
        viewModelScope.launch {
            _myInfoMutationEvent.emit(
                MyInfoMutationEvent.UpdateProfilePhoto(
                    Mutation(photo, UpdateProfilePhoto().run(photo))
                )
            )
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
): D? {
    state.isAccessible = true
    return runCatching {
        runMethod(param)
    }.onSuccess { data ->
        state.call().update {
            UiState(data = data, loading = false, error = null)
        }
    }.onFailure { e ->
        state.call().update {
            UiState(data = null, loading = false, error = e)
        }
    }.getOrNull()
}