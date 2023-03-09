package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.model.EmailVerifyResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface EmailService {
    /**
     * 회원가입 시에 이메일 인증을 위한 API
     * @param body {email:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("email")
    suspend fun sendRegisterEmail(@Body body: RequestBody)

    /**
     * 비밀번호 재설정 시에 이메일 인증을 위한 API
     * @param body {email:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("email/password")
    suspend fun sendForgotPWEmail(@Body body: RequestBody)

    /**
     * 회원가입 시에 인증코드 검증을 위한 API
     * @param email 인증코드 발송 시 사용한 이메일
     * @param body {code:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("verifyCode/{email}")
    suspend fun verifyRegisterCode(@Path("email") email:String, @Body body: RequestBody)

    /**
     * 비밀번호 변경 시에 인증코드 검증을 위한 API
     * @param email 인증코드 발송 시 사용한 이메일
     * @param body {code:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("verifyCode/password/{email}")
    suspend fun verifyForgotPWEmail(@Path("email") email:String, @Body body: RequestBody)
}
