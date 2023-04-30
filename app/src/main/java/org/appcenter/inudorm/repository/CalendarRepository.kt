package org.appcenter.inudorm.repository

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.appcenter.inudorm.model.Calendar
import org.appcenter.inudorm.model.CalendarsRequestDto
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.networking.RetrofitInstance
import retrofit2.Retrofit
import retrofit2.http.GET

class CalendarRepository {
    suspend fun getCalendars(body: CalendarsRequestDto): ArrayList<Calendar> {
        return RetrofitInstance.service.getCalendars(body)
    }


}