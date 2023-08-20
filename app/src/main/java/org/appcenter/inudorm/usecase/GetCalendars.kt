package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.ScheduleData
import org.appcenter.inudorm.model.SchedulesRequestDto
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat

val getCalendarDateFormat = SimpleDateFormat("yyyy-MM")

class GetCalendars : ResultUseCase<Nothing?, ArrayList<Schedule>>() {
    override suspend fun onExecute(params: Nothing?): ArrayList<Schedule> {
        val date = LocalDateTime.now().toDate()
        val yearMonth = getCalendarDateFormat.format(date)
        val param = SchedulesRequestDto(yearMonth)
        val officialSchedules = calendarRepository.getSchedules(param)
//        val teamSchedules = calendarRepository.getMonthlyTeamSchedules(param)
        val schedules = ArrayList<Schedule>()
        schedules.addAll(officialSchedules)
//        schedules.addAll(teamSchedules)
        return schedules

    }
}