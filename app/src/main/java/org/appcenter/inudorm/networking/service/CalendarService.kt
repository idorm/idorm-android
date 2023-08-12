package org.appcenter.inudorm.networking.service

import org.appcenter.inudorm.model.*
import retrofit2.http.*

interface CalendarService {
    companion object {
        const val path = "api/v1"
    }

    @POST("${path}/member/calendars")
    suspend fun getCalendars(@Body body: SchedulesRequestDto): ArrayList<Schedule>

    @GET("${path}/member/team/calendar")
    suspend fun getTeamCalendar(@Query("teamCalendarId") teamScheduleId: Long): TeamSchedule

    @PUT("${path}/member/team/calendar")
    suspend fun editTeamSchedule(@Body body: ScheduleUpdateDto): TeamSchedule

    @POST("${path}/member/team/calendar")
    suspend fun createTeamSchedule(@Body body: ScheduleUpdateDto): TeamSchedule

    @DELETE("${path}/member/team/calendar")
    suspend fun deleteTeamSchedule(@Query("teamCalendarId") teamScheduleId: Long): TeamSchedule

    @PUT("${path}/member/team/calendar/sleepover")
    suspend fun editSleepOverSchedule(@Body body: SleepOverUpdateDto): TeamSchedule

    @POST("${path}/member/team/calendar/sleepover")
    suspend fun createSleepOverSchedule(@Body body: SleepOverUpdateDto): TeamSchedule

    @POST("${path}/member/team/calendars")
    suspend fun getMonthlyTeamCalendars(@Body body: SchedulesRequestDto) : ArrayList<TeamSchedule>
}