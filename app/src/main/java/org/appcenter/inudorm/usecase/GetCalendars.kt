package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.ScheduleData
import org.appcenter.inudorm.model.SchedulesRequestDto
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

val getCalendarDateFormat = SimpleDateFormat("yyyy-MM")

class GetCalendars : ResultUseCase<LocalDate, List<Schedule>>() {
    override suspend fun onExecute(date: LocalDate): List<Schedule> {
        val month = date.monthValue.toString().padStart(2, '0')
        val param = SchedulesRequestDto("${date.year}-${month}")
        val officialSchedules = calendarRepository.getSchedules(param)
//        val teamSchedules = calendarRepository.getMonthlyTeamSchedules(param)
        val schedules = ArrayList<Schedule>()
        schedules.addAll(officialSchedules)
//        schedules.addAll(teamSchedules)
        return schedules

    }
}