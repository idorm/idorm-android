package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.usecase.GetLikedMates

data class MateListState(var sortBy: String, var mates: UiState<ArrayList<MatchingInfo>>)

class LikedMateListViewModel : ViewModel() {
    val _mateListState = MutableStateFlow(MateListState("addedAtDesc", UiState()))
    val mateListState: StateFlow<MateListState>
        get() = _mateListState

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
}