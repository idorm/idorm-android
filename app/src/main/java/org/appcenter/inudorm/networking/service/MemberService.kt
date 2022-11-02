package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.networking.Data
import retrofit2.http.*
import java.lang.reflect.Member

interface MemberService{

    /**
     * 로그인 API
     * @param body {email:String, password:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("login")
    suspend fun login(@Body body: RequestBody): Data<Boolean>

    /**
     * 회원가입 API
     * @param body {email:String, password:String} Json RequestBody object
     * @return data가 빈 Data
     */
    @POST("register")
    suspend fun register(@Body body: RequestBody): Data<Boolean>

    /**
     * 로그인 후 토큰을 이용해 정보를 가져오는 API
     * @return data가 사용자 정보인 Data
     */
    @GET("member")
    suspend fun lookupMemberInfo(): Data<Member>

    /**
     * 로그인된 사용자의 토큰을 이용해 사용자를 제거하는 API
     * 가입 시 사용했던 이메일도 제거 => 재인증 필요
     * @return data가 빈 Data
     */
    @DELETE("member")
    suspend fun withdrawMember(): Data<Nothing>

    /**
     * 로그인된 사용자 정보를 업데이트 하는 API
     * @param body {nickname: String, password: String} Json RequestBody Object
     * @return data가 빈 Data
     */
    @PATCH("member")
    suspend fun updateMember(@Body body:RequestBody): Data<Nothing>

    /**
     * 로그인된 사용자 정보 중 닉네임만 업데이트 하는 API
     * @param body {nickname: String} Json RequestBody Object
     * @return data가 빈 Data
     */
    @PATCH("member/nickname")
    suspend fun updateNickname(@Body body:RequestBody): Data<Nothing>

    /**
     * 로그인된 사용자 정보 중 비밀번호만 업데이트 하는 API
     * @param body {password: String} Json RequestBody Object
     * @return data가 빈 Data
     */
    @PATCH("member/password")
    suspend fun updatePassword(@Body body:RequestBody): Data<Nothing>

}