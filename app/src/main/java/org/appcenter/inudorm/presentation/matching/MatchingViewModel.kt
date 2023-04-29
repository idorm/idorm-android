package org.appcenter.inudorm.presentation.matching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App.Companion.localFilterRepository
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.presentation.board.Content
import org.appcenter.inudorm.usecase.*
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State

// Todo: Change Member type
data class MatchingState(
    var isLoading: Boolean = false,
    var error: Throwable? = null,
    var mates: ArrayList<MatchingInfo> = ArrayList(),
    var filter: RoomMateFilter,
    var loadMode: LoadMode = LoadMode.Prepare,
)

enum class LoadMode {
    Prepare,
    Paging,
    Update
}

sealed class MutationEvent(open val mutation: Mutation<*, *>) {}
sealed class UserMutationEvent(override val mutation: Mutation<*, *>) : MutationEvent(mutation) {
    data class AddLikedMatchingInfo(override val mutation: Mutation<MutateFavoriteRequestDto, Boolean>) :
        UserMutationEvent(mutation)

    data class DeleteLikedMatchingInfo(override val mutation: Mutation<MutateFavoriteRequestDto, Boolean>) :
        UserMutationEvent(mutation)

    class AddDislikedMatchingInfo(override val mutation: Mutation<MutateFavoriteRequestDto, Boolean>) :
        UserMutationEvent(mutation)

    class DeleteDislikedMatchingInfo(override val mutation: Mutation<MutateFavoriteRequestDto, Boolean>) :
        UserMutationEvent(mutation)

    class ReportMatchingInfo(override val mutation: Mutation<ReportRequestDto, Boolean>) :
        UserMutationEvent(mutation)

    class SetMatchingInfoVisibility(override val mutation: Mutation<Boolean, Boolean>) :
        UserMutationEvent(mutation)
}

data class Mutation<P, R>(val request: P, val state: State<R>)


class MatchingViewModel : ViewModel() {
    private val _matchingState: MutableStateFlow<MatchingState> = MutableStateFlow(
        MatchingState(
            mates = arrayListOf(),
            filter = localFilterRepository.roomMateFilter,
        )
    )
    val matchingState: StateFlow<MatchingState>
        get() = _matchingState


    private val _userMutationEvent: MutableStateFlow<UserMutationEvent?> = MutableStateFlow(null)
    val userMutationEvent: MutableStateFlow<UserMutationEvent?>
        get() = _userMutationEvent

    fun refresh() {
        getMates(LoadMode.Update, size = 10)
    }


    /*
    * Todo: Persons Data (for matching)
    * Todo: Pass and Like feature
    */
    // TODO: Implement the ViewModel
    fun getMates(
        loadMode: LoadMode,
        filter: RoomMateFilter = _matchingState.value.filter,
        size: Int,
    ) {
        _matchingState.update {
            it.copy(
                isLoading = true,
                loadMode = loadMode,
                filter = filter
            )
        }
        viewModelScope.launch {
            kotlin.runCatching {
                GetRoomMates().run(matchingState.value.filter)
            }.onSuccess { mates ->
                IDormLogger.i(this@MatchingViewModel, "성공!!")
                _matchingState.update {
                    it.copy(
                        isLoading = false,
                        mates = mates,
                        loadMode = loadMode,
                        error = null
                    )
                }
            }.onFailure { err ->
                IDormLogger.i(this@MatchingViewModel, err.toString())
                _matchingState.update {
                    it.copy(
                        isLoading = false,
                        error = err,
                        loadMode = loadMode,
                    )
                }
            }
        }
    }

    /*
        Todo: 좋아요/싫어요된 카드는 fill 해주기
        Todo: 좋아요/싫어요 할떄는 지우기
     */

    fun addLikedMate(id: Int) {
        viewModelScope.launch {
            val params = MutateFavoriteRequestDto(id, true)

            _userMutationEvent.emit(
                UserMutationEvent.AddLikedMatchingInfo(
                    Mutation(
                        params,
                        AddLikedOrDislikedMatchingInfo().run(params)
                    )
                )
            )
        }
    }

    fun deleteLikedMate(id: Int) {
        viewModelScope.launch {

            val params = MutateFavoriteRequestDto(id, true)
            _userMutationEvent.emit(
                UserMutationEvent.DeleteLikedMatchingInfo(
                    Mutation(
                        params,
                        DeleteLikeOrDislikeMatchingInfo().run(params)
                    )
                )
            )
        }
    }

    fun addDislikedMate(id: Int) {
        viewModelScope.launch {
            val params = MutateFavoriteRequestDto(id, false)
            _userMutationEvent.emit(
                UserMutationEvent.AddDislikedMatchingInfo(
                    Mutation(
                        params,
                        AddLikedOrDislikedMatchingInfo().run(params)
                    )
                )
            )
        }
    }

    fun deleteDislikedMate(id: Int) {
        viewModelScope.launch {
            val params = MutateFavoriteRequestDto(id, false)
            _userMutationEvent.emit(
                UserMutationEvent.DeleteDislikedMatchingInfo(
                    Mutation(
                        params,
                        DeleteLikeOrDislikeMatchingInfo().run(params)
                    )
                )
            )
        }
    }

    fun reportMatchingInfo(id: Int, reason: String, reasonType: String, reportType: Content) {
        viewModelScope.launch {
            val params = ReportRequestDto(id, reason, reasonType, reportType)
            _userMutationEvent.emit(
                UserMutationEvent.ReportMatchingInfo(
                    Mutation(params, Report().run(params))
                )
            )
        }
    }

    fun setMatchingInfoVisibility(isMatchingInfoPublic: Boolean) {
        viewModelScope.launch {
            _userMutationEvent.emit(
                UserMutationEvent.SetMatchingInfoVisibility(
                    Mutation(
                        isMatchingInfoPublic,
                        SetMatchingInfoVisibility().run(isMatchingInfoPublic)
                    )
                )
            )
            _matchingState.update {
                it.copy(
                    error = null
                )
            }
            getMates(LoadMode.Update, size = 10)
        }
    }
}

class MatchingViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchingViewModel::class.java)) {
            return MatchingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}