package org.appcenter.inudorm.repository

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.TeamScheduleParams

class CalendarRepository {
    suspend fun getSchedules(body: SchedulesRequestDto): ArrayList<Schedule> {
        return RetrofitInstance.service.getCalendars(body)
    }

    suspend fun getTeamSchedule(teamScheduleId: Int): TeamSchedule {
        return RetrofitInstance.service.getTeamCalendar(teamScheduleId)
    }

    suspend fun createTeamSchedule(params: TeamScheduleParams): TeamSchedule {
        val str = App.gson.toJson(params.teamSchedule)
        return RetrofitInstance.service.createTeamSchedule(createJsonRequestBody(str))
    }

    suspend fun editTeamSchedule(params: TeamScheduleParams): TeamSchedule {
        val str = App.gson.toJson(params.teamSchedule)
        return RetrofitInstance.service.createTeamSchedule(createJsonRequestBody(str))
    }

    suspend fun deleteTeamSchedule(teamScheduleId: Long) {
        return RetrofitInstance.service.deleteTeamSchedule(teamScheduleId)
    }

    suspend fun editSleepOverSchedule(body: SleepOverUpdateDto): TeamSchedule {
        return RetrofitInstance.service.editSleepOverSchedule(body)
    }

    suspend fun createSleepOverSchedule(body: SleepOverUpdateDto): TeamSchedule {
        return RetrofitInstance.service.createSleepOverSchedule(body)
    }

    suspend fun getMonthlyTeamSchedules(body: SchedulesRequestDto): ArrayList<TeamSchedule> {
        return RetrofitInstance.service.getMonthlyTeamCalendars(body)
    }
}