package org.appcenter.inudorm.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.appcenter.inudorm.util.IDormLogger
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



//    fun onDateChanged(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
//        IDormLogger.i(this, "${selected} | ${date.day}")
//        if (selected) {
//            _selectedDay.value = date.day
//            _selectedDay.postValue(date.day)
//        }
//    }
}
