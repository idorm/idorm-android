package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.Calendar
import org.appcenter.inudorm.model.CalendarsRequestDto
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat

class GetCalendars : ResultUseCase<Nothing?, ArrayList<Calendar>>() {
    override suspend fun onExecute(params: Nothing?): ArrayList<Calendar> {
        val date = LocalDateTime.now().toDate()
        val format = SimpleDateFormat("yyyy-MM")
        val yearMonth = format.format(date)
        val param = CalendarsRequestDto(yearMonth)
        return calendarRepository.getCalendars(param)
    }
}