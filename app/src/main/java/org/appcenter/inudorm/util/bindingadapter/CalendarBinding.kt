package org.appcenter.inudorm.util.bindingadapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.presentation.adapter.CalendarAdapter
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import org.joda.time.LocalDateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
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
                LocalDateTime.parse(stringDate, DateTimeFormat.forPattern("yyyy-MM-dd"))
                    .toDate()
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
            // FIXME: currentDate 가 현재 시스템 날짜인지 선택된 날짜인지 탭 형태로 넘긴 달 페이지인지 모름
            val date = this.currentDate

            // Todo: 현재 방식은 currentDate에 의존적임.
            IDormLogger.i(this, selectedDay.toString())
            this.selectedDate = CalendarDay.from(date.year, date.month, selectedDay)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:onDateChanged"], requireAll = false)
    fun MaterialCalendarView.onSelectDay(attrChanged: InverseBindingListener?) {
        IDormLogger.i(this, "date change listener setted")
        setOnDateChangedListener { widget, date, selected ->
            attrChanged?.onChange()
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedDay", event = "android:onDateChanged")
    fun MaterialCalendarView.getSelectedDay(): Int {
        IDormLogger.i(this, "${selectedDate?.year}/${selectedDate?.month}/${selectedDate?.day}")
        return selectedDate?.day ?: 1
//        return if (selectedDate != null)
//            LocalDate.parse("${selectedDate?.year}/${selectedDate?.month}/${selectedDate?.day}")
//        else LocalDate.now()
    }


}