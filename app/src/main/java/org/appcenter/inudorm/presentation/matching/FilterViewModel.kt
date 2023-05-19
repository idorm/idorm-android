package org.appcenter.inudorm.presentation.matching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.RoomMateFilter

class FilterViewModel(private val initialFilter: RoomMateFilter) : ViewModel() {
    private val _filterState = MutableStateFlow(initialFilter)
    val filterState: StateFlow<RoomMateFilter>
        get() = _filterState

    fun clear() {
        viewModelScope.launch {
            _filterState.update {
                initialFilter
            }
        }
    }

}

class FilterViewModelFactory(private val initialFilter: RoomMateFilter) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(initialFilter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}