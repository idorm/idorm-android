package org.appcenter.inudorm.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

data class MatchingState(
    var isLoading: Boolean = false,
    var mates: String = "",
)

class MatchingViewModel : ViewModel() {
    private val _matchingState: MutableStateFlow<MatchingState> = MutableStateFlow(MatchingState())
    val matchingState: StateFlow<MatchingState>
        get() = _matchingState
    /*
    * Todo: Persons Data (for matching)
    * Todo: Pass and Like feature
    */
    // TODO: Implement the ViewModel
}