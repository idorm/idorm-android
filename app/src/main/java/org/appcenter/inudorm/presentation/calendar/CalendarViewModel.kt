package org.appcenter.inudorm.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.usecase.GetTeamSchedules
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.time.LocalDate


class CalendarViewModel : ViewModel() {
    private val _selectedDay = MutableLiveData<Int>(LocalDate.now().dayOfMonth)
    val selectedDay: LiveData<Int> get() = _selectedDay




    val schedules: MutableStateFlow<State<List<TeamSchedule>>> =
        MutableStateFlow(State.Initial())

    fun getSchedules(yearMonth: String) {
        // Todo: Get Schedules
        viewModelScope.launch {
            if (schedules.value is State.Loading) return@launch
            schedules.emit(State.Loading())
            schedules.emit(GetTeamSchedules().run(yearMonth))
        }
    }


//    fun onDateChanged(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
//        IDormLogger.i(this, "${selected} | ${date.day}")
//        if (selected) {
//            _selectedDay.value = date.day
//            _selectedDay.postValue(date.day)
//        }
//    }
}
