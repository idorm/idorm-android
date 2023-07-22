package org.appcenter.inudorm.networking.service

import org.appcenter.inudorm.model.*
import retrofit2.http.*

interface CalendarService {
    @POST("member/calendars")
    suspend fun getCalendars(@Body body: SchedulesRequestDto): ArrayList<Schedule>

    @GET("member/team/calendar")
    suspend fun getTeamCalendar(@Query("teamCalendarId") teamScheduleId: Long): TeamSchedule

    @PUT("member/team/calendar")
    suspend fun editTeamSchedule(@Body body: ScheduleUpdateDto): TeamSchedule

    @POST("member/team/calendar")
    suspend fun createTeamSchedule(@Body body: ScheduleUpdateDto): TeamSchedule

    @DELETE("member/team/calendar")
    suspend fun deleteTeamSchedule(@Query("teamCalendarId") teamScheduleId: Long): TeamSchedule

    @PUT("member/team/calendar/sleepover")
    suspend fun editSleepOverSchedule(@Body body: SleepOverUpdateDto): TeamSchedule

    @POST("member/team/calendar/sleepover")
    suspend fun createSleepOverSchedule(@Body body: SleepOverUpdateDto): TeamSchedule

    @POST("member/team/calendars")
    suspend fun getMonthlyTeamCalendars(@Body body: SchedulesRequestDto) : ArrayList<TeamSchedule>
}