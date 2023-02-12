package org.appcenter.inudorm.networking

import okhttp3.RequestBody
import org.appcenter.inudorm.networking.service.*

/**
 * 여러 서비스들을 합친 Service Interface 입니다.
 */
interface Service : EmailService, MatchingInfoService, MemberService, MatchingMateService, CommunityService

fun createJsonRequestBody(content: String): RequestBody =
    RequestBody.create(
        okhttp3.MediaType.parse("application/json; charset=utf-8"),
        content
    )