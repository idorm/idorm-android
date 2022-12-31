package org.appcenter.inudorm.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App.Companion.localFilterRepository
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.usecase.*
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.Event
import org.appcenter.inudorm.util.IDormLogger
import java.util.*

// Todo: Change Member type
data class MatchingState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var mates: ArrayList<MatchingInfo> = ArrayList(),
    var filter: RoomMateFilter,
    var loadMode: LoadMode = LoadMode.Prepare,
)

enum class LoadMode {
    Prepare,
    Paging,
    Update
}

sealed class UserPreferenceEvent {
    data class AddLikedMatchingInfo(val id: Int, val success: Boolean) :
        UserPreferenceEvent();
    data class AddDislikedMatchingInfo(val id: Int, val success: Boolean) :
        UserPreferenceEvent();
}

class MatchingViewModel : ViewModel() {
    private val _matchingState: MutableStateFlow<MatchingState> = MutableStateFlow(
        MatchingState(
            mates = arrayListOf(),
            filter = localFilterRepository.roomMateFilter,
        )
    )
    val matchingState: StateFlow<MatchingState>
        get() = _matchingState

    private val _userPreferenceEvent: MutableSharedFlow<UserPreferenceEvent> = MutableSharedFlow()
    val userPreferenceEvent: SharedFlow<UserPreferenceEvent>
        get() = _userPreferenceEvent

    /*
    * Todo: Persons Data (for matching)
    * Todo: Pass and Like feature
    */
    // TODO: Implement the ViewModel
    fun getMates(
        loadMode: LoadMode,
        filter: RoomMateFilter = _matchingState.value.filter,
        size: Int
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
                    )
                }
            }.onFailure { err ->
                IDormLogger.i(this@MatchingViewModel, err.toString())
                _matchingState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = err.message,
                        loadMode = loadMode
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
            kotlin.runCatching {
                AddLikedMatchingInfo().run(id)
            }.onSuccess {
                _userPreferenceEvent.emit(
                    UserPreferenceEvent.AddLikedMatchingInfo(
                        id,
                        true,
                    )
                )
            }
        }
    }

    fun deleteLikedMate(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                DeleteLikedMatchingInfo().run(id)
            }.onSuccess {
            }
        }
    }

    fun addDislikedMate(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                AddDislikedMatchingInfo().run(id)
            }.onSuccess {
                _userPreferenceEvent.emit(
                    UserPreferenceEvent.AddDislikedMatchingInfo(
                        id,
                        true,
                    )
                )
            }
        }
    }

    fun deleteDislikedMate(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                DeleteDislikedMatchingInfo().run(id)
            }.onSuccess {
            }
        }
    }

}

class MatchingViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchingViewModel::class.java)) {
            return MatchingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}