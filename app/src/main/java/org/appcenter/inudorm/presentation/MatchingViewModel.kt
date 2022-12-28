package org.appcenter.inudorm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App.Companion.localFilterRepository
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.usecase.GetRoomMates
import java.util.*

// Todo: Change Member type
data class MatchingState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var mates: ArrayList<MatchingInfo> = ArrayList(),
    var filter: RoomMateFilter
)

class MatchingViewModel : ViewModel() {
    private val _matchingState: MutableStateFlow<MatchingState> = MutableStateFlow(
        MatchingState(
            mates = arrayListOf(),
            filter = localFilterRepository.roomMateFilter
        )
    )
    val matchingState: StateFlow<MatchingState>
        get() = _matchingState

    /*
    * Todo: Persons Data (for matching)
    * Todo: Pass and Like feature
    */
    // TODO: Implement the ViewModel
    fun getMates(size: Int) {
        _matchingState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            kotlin.runCatching {
                GetRoomMates().run(matchingState.value.filter)
            }.onSuccess { mates ->
                _matchingState.update {
                    it.copy(
                        isLoading = false,
                        mates = mates
                    )
                }
            }.onFailure {
                _matchingState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = it.errorMessage
                    )
                }
            }
        }
    }

    fun setFilter(filter: RoomMateFilter) {
        _matchingState.update {
            it.copy(
                filter = filter
            )
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