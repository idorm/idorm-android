package org.appcenter.inudorm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.Mate
import org.appcenter.inudorm.model.Member
import org.appcenter.inudorm.model.MyInfo
import org.appcenter.inudorm.repository.RoomMateRepository
import org.appcenter.inudorm.repository.testMyInfo
import org.appcenter.inudorm.usecase.GetRoomMates

// Todo: Change Member type
data class MatchingState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var mates: ArrayList<Mate> = ArrayList(),
)

class MatchingViewModel(private val roomMateRepository: RoomMateRepository) : ViewModel() {
    private val _matchingState: MutableStateFlow<MatchingState> = MutableStateFlow(MatchingState())
    val matchingState: StateFlow<MatchingState>
        get() = _matchingState

    /*
    * Todo: Persons Data (for matching)
    * Todo: Pass and Like feature
    */
    // TODO: Implement the ViewModel
    fun getMates(size: Int) {
        val currentMates = _matchingState.value.mates
        _matchingState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            kotlin.runCatching {
                GetRoomMates(roomMateRepository).run(testMyInfo)
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
}

class MatchingViewModelFactory(private val roomMateRepository: RoomMateRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchingViewModel::class.java)) {
            return MatchingViewModel(roomMateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}