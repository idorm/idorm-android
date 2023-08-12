package org.appcenter.inudorm.presentation.calendar

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate


class EventDecorator(private val color: Int, dates: Collection<CalendarDay>?) :
    DayViewDecorator {
    private val dates: HashSet<CalendarDay>

    init {
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5f, color))
    }
}

class TodayDecorator(private val color: Int) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return LocalDate.of(day.year, day.month, day.day) == LocalDate.now()
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(2f, color))
    }
}