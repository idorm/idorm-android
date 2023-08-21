package org.appcenter.inudorm.networking.service

import org.appcenter.inudorm.model.RoomMateTeamResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface RoomMateTeamService {
    @GET("api/v1/member/team")
    suspend fun getMyRoomMates(): RoomMateTeamResponseDto

    @POST("api/v1/member/team")
    suspend fun acceptInvitation(@Query("registerMemberId") memberId: Int)

    @DELETE("api/v1/member/team")
    suspend fun kickRoomMateMember(@Query("memberId") memberId: Int)

    /**
     * 최후의 남은 팀원 1명이 팀 폭발했음을 확인했을 때 해당 API를 요청해주세요.
     */
    @PATCH("api/v1/member/team")
    suspend fun assumeTeamDisorganization()


}