package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.SelectItem

data class BoardUiState(val selectedDorm: SelectItem)

class BoardViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _boardUiState =
        MutableStateFlow(BoardUiState(SelectItem("제${Dorm.DORM1.text}기숙사", Dorm.DORM1.text)))
    val boardUiState: StateFlow<BoardUiState>
        get() = _boardUiState

    fun setDorm(dorm: SelectItem) {
        _boardUiState.update {
            it.copy(
                selectedDorm = dorm
            )
        }
    }
}
