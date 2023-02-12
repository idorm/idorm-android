package org.appcenter.inudorm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import org.appcenter.inudorm.model.RoomMateFilter

class FilterViewModel(initialFilter: RoomMateFilter) : ViewModel() {
    private val _filterState = MutableStateFlow(initialFilter)
    val filterState = _filterState
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