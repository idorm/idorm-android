package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.model.ChangeNickNameDto
import org.appcenter.inudorm.model.ChangePasswordDto
import org.appcenter.inudorm.model.User
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Member

interface MemberService {

    /**
     * 로그인 API
     * @param body {email:String, password:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @GET("member")
    suspend fun loginRefresh(): User

    /**
     * 로그인 API
     * @param body {email:String, password:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("/v2/login")
    suspend fun login(@Body body: RequestBody, @Header("fcm-token") fcmKey: String): Response<User>

    /**
     * 회원가입 API
     * @param body {email:String, password:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("register")
    suspend fun register(@Body body: RequestBody): User

    /**
     * 로그인 후 토큰을 이용해 정보를 가져오는 API
     * FIXME: loginRefresh와 겹침.
     * @return data가 사용자 정보인 Data
     */
    @GET("member")
    suspend fun lookupMemberInfo(): Member

    /**
     * 로그인된 사용자의 토큰을 이용해 사용자를 제거하는 API
     * 가입 시 사용했던 이메일도 제거 => 재인증 필요
     * @return data가 빈 Data
     */
    @DELETE("member")
    suspend fun withdrawMember(): Response<List<Any>>

    /**
     * 로그인된 사용자 정보를 업데이트 하는 API
     * @param body {nickname: String, password: String} Json RequestBody Object
     * @return data가 빈 Data
     */
    @PATCH("member")
    suspend fun updateMember(@Body body: RequestBody): Nothing

    /**
     * 로그인된 사용자 정보 중 닉네임만 업데이트 하는 API
     * @param body {nickname: String} Json RequestBody Object
     * @return data가 빈 Data
     */
    @PATCH("member/nickname")
    suspend fun changeNickname(@Body body: ChangeNickNameDto)

    /**
     * 로그인된 사용자 정보 중 비밀번호만 업데이트 하는 API
     */
    @PATCH("member/password")
    suspend fun changePassword(@Body body: ChangePasswordDto): User

    @PATCH("member/fcm")
    suspend fun updateFcmToken(@Header("fcm-token") token: String)

}