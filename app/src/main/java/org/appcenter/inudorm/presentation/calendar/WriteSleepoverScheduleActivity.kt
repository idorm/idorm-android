package org.appcenter.inudorm.presentation.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import com.kizitonwose.calendar.view.ViewContainer
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteSleepoverScheduleBinding
import org.appcenter.inudorm.util.IDormLogger
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class WriteSleepoverScheduleActivity : LoadingActivity() {

    private val binding: ActivityWriteSleepoverScheduleBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_sleepover_schedule)
    }

    var selectedDay: LocalDate = LocalDate.now()
    fun dateClicked(date: LocalDate) {
        binding.calendarView.notifyDateChanged(selectedDay)
        selectedDay = date
        binding.calendarView.notifyDateChanged(date)
    }

    private fun bindCalendar() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarDayText)
            lateinit var day: CalendarDay

            init {
                IDormLogger.i(this, "setting up click listener!")
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate)
                        dateClicked(date = day.date)
                }
            }
        }

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    container.textView.setTextColor(
                        ContextCompat.getColor(
                            this@WriteSleepoverScheduleActivity,
                            R.color.iDorm_gray_200
                        )
                    )
                } else {
                    when (data.date) {
                        selectedDay -> {
                            container.view.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@WriteSleepoverScheduleActivity,
                                    R.color.iDorm_blue
                                )
                            )
                        }

                        else
                            // 아마 RecyclerView를 이용하는 특성상 몇개 캘린더를 재활용하는 형태라서 위에서 setBackgroundResource를 하면 4개월 주기로 다른 날에도 뜸
                        -> container.view.setBackgroundColor(
                            ContextCompat.getColor(
                                this@WriteSleepoverScheduleActivity,
                                R.color.white
                            )
                        )
                    }

                }
            }
        }




        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {

                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    val prev = container.titlesContainer.findViewById<ImageView>(R.id.prev)
                    val next = container.titlesContainer.findViewById<ImageView>(R.id.next)
                    val monthTitle =
                        container.titlesContainer.findViewById<TextView>(R.id.monthTitle)

                    prev.setOnClickListener {
                        binding.calendarView.scrollToMonth(data.yearMonth.previousMonth)
                    }
                    next.setOnClickListener {
                        binding.calendarView.scrollToMonth(data.yearMonth.nextMonth)
                    }
                    monthTitle.text = "${data.yearMonth.monthValue}월"

                    container.titlesContainer.findViewById<LinearLayout>(R.id.weekDayTitlesContainer).children
                        .map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek()[index]
                            val title =
                                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
                            textView.text = title
                            if (index % 6 == 0)
                                textView.setTextColor(
                                    ContextCompat.getColor(
                                        this@WriteSleepoverScheduleActivity,
                                        R.color.iDorm_red
                                    )
                                )
                        }
                }

                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)


            }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_sleepover_schedule)

        bindCalendar()
    }
}