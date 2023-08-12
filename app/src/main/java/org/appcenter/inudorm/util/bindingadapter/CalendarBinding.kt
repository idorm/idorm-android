package org.appcenter.inudorm.util.bindingadapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.presentation.adapter.CalendarAdapter
import org.appcenter.inudorm.presentation.calendar.EventDecorator
import org.appcenter.inudorm.presentation.calendar.mateColors
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

object CalendarBinding {
    @JvmStatic
    @BindingAdapter("date")
    fun TextView.bindDate(stringDate: String?) {
        if (stringDate.isNullOrEmpty()) {
            return
        }
        try {

            val date =
                SimpleDateFormat("yyyy-MM-dd").parse(stringDate)
            val format = SimpleDateFormat("MM월 dd일")
            text = format.format(date)

        } catch (e: java.lang.RuntimeException) {
            visibility = View.GONE
        }

    }

    @JvmStatic
    @BindingAdapter("time")
    fun TextView.bindTime(stringTime: String?) {
        if (stringTime.isNullOrEmpty()) {
            return
        }

        try {
            val regex = Regex("(\\d{2}):(\\d{2}):(\\d{2})")
            val list = regex.find("16:40:20")?.groupValues!!
            val hour = list[1].toInt()
            val min = list[2].toInt()
            val am_pm = if (hour < 12) "AM" else "PM"
            text = "$am_pm $hour:$min"
        } catch (e: java.lang.RuntimeException) {
            visibility = View.GONE
        }
    }


    @JvmStatic
    @BindingAdapter("calendarState")
    fun RecyclerView.bindCalendarState(state: State<ArrayList<Schedule>>) {
        if (state !is State.Initial)
            if (adapter is CalendarAdapter && !state.isLoading() && state is State.Success) {
                val a = adapter as CalendarAdapter
                a.dataSet = ArrayList(state.data!!)
                // 리스트에 추가하고
                a.notifyDataSetChanged()
            }
    }

    @JvmStatic
    @BindingAdapter("dormVisibility")
    fun Chip.dormVisibility(visible: Boolean?) {
        visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("selectedDay")
    fun MaterialCalendarView.bindSelectedDay(selectedDay: Int?) {
        if (selectedDay != null) {
            val currentDate = LocalDate.now()
            val calendarDay = CalendarDay.from(
                selectedDate?.year ?: currentDate.monthValue,
                selectedDate?.month ?: currentDate.dayOfMonth,
                selectedDay
            )
            this.selectedDate = calendarDay
        }
    }

    @JvmStatic
    @BindingAdapter("onDateSelected")
    fun MaterialCalendarView.bindListener(listener: OnDateSelectedListener?) {
        if (listener != null) {
            IDormLogger.i(this, "binding adapter! ")
            this.setOnDateChangedListener(listener)

        }
    }

    @JvmStatic
    @BindingAdapter("schedules")
    fun MaterialCalendarView.bindSchedules(_state: State<List<TeamSchedule>>) {
        // 팀원 별로 데코레이터를 달아줘야함.
        // 스케쥴 별 -> 팀원 별로 변환 필요
        // Todo: 에러/로딩 처리
        if (_state is State.Loading) return
        if (_state is State.Error) return
        val memberToScheduleMap = mutableMapOf<Int, List<TeamSchedule>>()
        val state = (_state as State.Success)
        state.data?.forEach { schedule ->
            schedule.targets.forEach { profile ->
                memberToScheduleMap.merge(profile.order!!, listOf(schedule)) { prev, curr ->
                    prev + curr
                }
            }
        }

        val decorators = memberToScheduleMap.map {
            val dates = it.value.map { schedule ->
                val date = LocalDate.parse(schedule.startDate)
                CalendarDay.from(date.year, date.monthValue, date.dayOfMonth)
            }
            EventDecorator(
                mateColors[it.key + 1],
                dates
            )
        }
        this.addDecorators(decorators)
    }


}