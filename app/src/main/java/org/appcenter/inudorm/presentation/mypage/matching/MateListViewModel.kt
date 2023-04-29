package org.appcenter.inudorm.presentation.mypage.matching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.presentation.board.Content
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.usecase.Report
import org.appcenter.inudorm.usecase.UseCase


data class Sortable<T>(var sortBy: String, var data: T)

abstract class MateListViewModel : ViewModel() {
    val _mateListState =
        MutableStateFlow(Sortable<UiState<ArrayList<MatchingInfo>>>("addedAtDesc", UiState()))
    val mateListState: StateFlow<Sortable<UiState<ArrayList<MatchingInfo>>>>
        get() = _mateListState
    val _userMutationState = MutableSharedFlow<UserMutationEvent>()
    val userMutationState: SharedFlow<UserMutationEvent>
        get() = _userMutationState
    abstract val getUseCase: UseCase<Nothing?, ArrayList<MatchingInfo>>

    fun getMates() {
        _mateListState.update {
            it.copy(
                data = it.data.copy(
                    loading = true
                )
            )
        }
        // Todo: Call UseCase
        viewModelScope.launch {
            kotlin.runCatching {
                getUseCase.run(null)
            }.onSuccess { mates ->
                _mateListState.update {
                    it.copy(
                        data = it.data.copy(
                            loading = false,
                            data = mates,
                            error = null,
                        )
                    )
                }
            }.onFailure { e ->
                _mateListState.update {
                    it.copy(
                        data = it.data.copy(
                            loading = false,
                            data = null,
                            error = e
                        )
                    )
                }
            }
        }
    }

    abstract fun deleteMate(id: Int)
    fun setSortBy(sortBy: String) {
        _mateListState.update {
            it.copy(sortBy = sortBy)
        }
    }

    fun reportMate(
        id: Int,
        reason: String,
        reasonType: String,
        reportType: Content,
    ) {
        viewModelScope.launch {
            val params = ReportRequestDto(id, reason, reasonType, reportType)
            _userMutationState.emit(
                UserMutationEvent.ReportMatchingInfo(
                    Mutation(params, Report().run(params))
                )
            )
        }
    }
}