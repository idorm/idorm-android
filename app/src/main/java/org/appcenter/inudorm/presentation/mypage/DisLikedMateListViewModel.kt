package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.util.IDormLogger

class DisLikedMateListViewModel : ViewModel() {
    private val _mateListState = MutableStateFlow(MateListState("addedAtDesc", UiState()))
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
}