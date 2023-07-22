package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.model.*
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Member

interface CalendarService {
    @POST("member/calendars")
    suspend fun getCalendars(@Body body: CalendarsRequestDto): ArrayList<Calendar>

    @GET("member/team/calendar")
    suspend fun getTeamCalendar(@Query("teamCalendarId") teamCalendarId: Long): TeamCalendar

    @PUT("member/team/calendar")
    suspend fun editTeamCalendar(@Body body: CalendarUpdateDto): TeamCalendar

    @POST("member/team/calendar")
    suspend fun createTeamCalendar(@Body body: CalendarUpdateDto): TeamCalendar

    @DELETE("member/team/calendar")
    suspend fun deleteTeamCalendar(@Query("teamCalendarId") teamCalendarId: Long): TeamCalendar

    @PUT("member/team/calendar/sleepover")
    suspend fun editSleepOverCalendar(@Body body: SleepOverUpdateDto): TeamCalendar

    @POST("member/team/calendar/sleepover")
    suspend fun createSleepOverCalendar(@Body body: SleepOverUpdateDto): TeamCalendar

    @POST("member/team/calendars")
    suspend fun getMonthlyTeamCalendars(@Body body: MonthlyTeamCalendarRequestDto)
}