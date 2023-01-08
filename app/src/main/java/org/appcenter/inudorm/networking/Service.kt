package org.appcenter.inudorm.networking

import okhttp3.RequestBody
import org.appcenter.inudorm.networking.service.EmailService
import org.appcenter.inudorm.networking.service.MatchingInfoService
import org.appcenter.inudorm.networking.service.MatchingMateService
import org.appcenter.inudorm.networking.service.MemberService
import org.appcenter.inudorm.usecase.UserInputParams
import org.json.JSONObject
import retrofit2.http.*

/**
 * 여러 서비스들을 합친 Service Interface 입니다.
 */
interface Service : EmailService, MatchingInfoService, MemberService, MatchingMateService

fun createJsonRequestBody(content: String): RequestBody =
    RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        content
    )