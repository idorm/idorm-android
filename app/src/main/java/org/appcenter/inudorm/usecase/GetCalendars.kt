package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.ScheduleData
import org.appcenter.inudorm.model.SchedulesRequestDto
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat

class GetCalendars : ResultUseCase<Nothing?, ArrayList<ScheduleData>>() {
    override suspend fun onExecute(params: Nothing?): ArrayList<ScheduleData> {
        val date = LocalDateTime.now().toDate()
        val format = SimpleDateFormat("yyyy-MM")
        val yearMonth = format.format(date)
        val param = SchedulesRequestDto(yearMonth)
        val officialSchedules = calendarRepository.getSchedules(param)
        val teamSchedules = calendarRepository.getMonthlyTeamSchedules(param)
        val schedules = ArrayList<ScheduleData>()
        schedules.addAll(officialSchedules)
        schedules.addAll(teamSchedules)
        return schedules

    }
}