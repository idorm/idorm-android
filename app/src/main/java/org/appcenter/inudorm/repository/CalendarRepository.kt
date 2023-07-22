package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.networking.RetrofitInstance

class CalendarRepository {
    suspend fun getSchedules(body: SchedulesRequestDto): ArrayList<Schedule> {
        return RetrofitInstance.service.getCalendars(body)
    }

    suspend fun getTeamSchedule(teamScheduleId: Long): TeamSchedule {
        return RetrofitInstance.service.getTeamCalendar(teamScheduleId)
    }

    suspend fun editTeamSchedule(body: ScheduleUpdateDto): TeamSchedule {
        return RetrofitInstance.service.editTeamSchedule(body)
    }

    suspend fun createTeamSchedule(body: ScheduleUpdateDto): TeamSchedule {
        return RetrofitInstance.service.createTeamSchedule(body)
    }

    suspend fun deleteTeamSchedule(teamScheduleId: Long): TeamSchedule {
        return RetrofitInstance.service.deleteTeamSchedule(teamScheduleId)
    }

    suspend fun editSleepOverSchedule(body: SleepOverUpdateDto): TeamSchedule {
        return RetrofitInstance.service.editSleepOverSchedule(body)
    }

    suspend fun createSleepOverSchedule(body: SleepOverUpdateDto): TeamSchedule {
        return RetrofitInstance.service.createSleepOverSchedule(body)
    }

    suspend fun getMonthlyTeamSchedules(body: SchedulesRequestDto) : ArrayList<TeamSchedule> {
        return RetrofitInstance.service.getMonthlyTeamCalendars(body)
    }
}