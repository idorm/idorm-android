package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.usecase.GetMatchingInfo

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