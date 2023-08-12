package org.appcenter.inudorm.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.coroutines.flow.MutableStateFlow
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.time.LocalDate


class CalendarViewModel : ViewModel() {
    private val _selectedDay = MutableLiveData<Int>(LocalDate.now().dayOfMonth)
    val selectedDay: LiveData<Int> get() = _selectedDay

    val onDateChanged =
        OnDateSelectedListener { _, date, selected ->
            IDormLogger.i(this, "$selected | ${date.day}")
            if (selected) {
                _selectedDay.value = date.day
                _selectedDay.postValue(date.day)
            }
        }


    val schedules: MutableStateFlow<State<List<TeamSchedule>>> =
        MutableStateFlow(State.Initial())

    fun getSchedules() {
        // Todo: Get Schedules
//        schedules.emit()
    }


//    fun onDateChanged(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
//        IDormLogger.i(this, "${selected} | ${date.day}")
//        if (selected) {
//            _selectedDay.value = date.day
//            _selectedDay.postValue(date.day)
//        }
//    }
}
