package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.model.*
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Member

interface CalendarService {
    @POST("member/calendars")
    suspend fun getCalendars(@Body body: CalendarsRequestDto): ArrayList<Calendar>
}