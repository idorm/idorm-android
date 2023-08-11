package org.appcenter.inudorm.presentation.calendar

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.joda.time.LocalDateTime


class CalendarViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val selectedDay = MutableLiveData(LocalDateTime.now().dayOfMonth)


}