package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteDislikedMatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.usecase.ReportMatchingInfo
import org.appcenter.inudorm.util.IDormLogger

class DisLikedMateListViewModel : ViewModel() {
    private val _mateListState = MutableStateFlow(MateListState("addedAtDesc", UiState()))
    val mateListState: StateFlow<MateListState>
        get() = _mateListState

    private val _userMutationState = MutableSharedFlow<UserMutationEvent>()
    val userMutationState: SharedFlow<UserMutationEvent>
        get() = _userMutationState

    fun getLikedMates() {
        _mateListState.update {
            it.copy(
                mates = it.mates.copy(
                    loading = true
                )
            )
        }
        // Todo: Call UseCase
        viewModelScope.launch {
            kotlin.runCatching {
                GetDisLikedMates().run(null)
            }.onSuccess { mates ->
                IDormLogger.i(this@DisLikedMateListViewModel, "성공")
                _mateListState.update {
                    it.copy(
                        mates = it.mates.copy(
                            loading = false,
                            data = mates,
                            error = null,
                        )
                    )
                }
            }.onFailure { e ->
                _mateListState.update {
                    it.copy(
                        mates = it.mates.copy(
                            loading = false,
                            data = null,
                            error = e
                        )
                    )
                }
            }
        }
    }

    fun deleteDislikedMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteDislikedMatchingInfo().run(id)
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteDislikedMatchingInfo(id, result != null)
            )
        }
    }

    fun reportMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                ReportMatchingInfo().run(id)
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.ReportMatchingInfo(id, result != null)
            )
        }
    }
}