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

class MyMatchingProfileViewModel : ViewModel() {
    private val _myPageState: MutableStateFlow<UiState<MatchingInfo>> = MutableStateFlow(UiState())
    val myPageState: StateFlow<UiState<MatchingInfo>>
        get() = _myPageState

    fun getMatchingInfo() {
        viewModelScope.launch {
            _myPageState.update {
                it.copy(
                    loading = true
                )
            }
            runCatch(this@MyMatchingProfileViewModel::_myPageState, GetMatchingInfo()::run, null)
        }
    }
}