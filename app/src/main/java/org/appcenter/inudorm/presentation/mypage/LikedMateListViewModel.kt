package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.*

data class MateListState(var sortBy: String, var mates: UiState<ArrayList<MatchingInfo>>)

class LikedMateListViewModel : ViewModel() {
    val _mateListState = MutableStateFlow(MateListState("addedAtDesc", UiState()))
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
                GetLikedMates().run(null)
            }.onSuccess { mates ->
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

    fun deleteLikedMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteLikedMatchingInfo().run(id)
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteLikedMatchingInfo(id, result != null)
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