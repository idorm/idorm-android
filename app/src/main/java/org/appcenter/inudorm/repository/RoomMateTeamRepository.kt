package org.appcenter.inudorm.repository

import kotlinx.coroutines.delay
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.RoomMateTeamResponseDto
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.usecase.MutateFavoriteRequestDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

class RoomMateTeamRepository {
    suspend fun getMyRoomMates(): RoomMateTeamResponseDto {
        return RetrofitInstance.service.getMyRoomMates()
    }

    suspend fun acceptInvitation(memberId: Int) {
        return RetrofitInstance.service.acceptInvitation(memberId)
    }

    suspend fun kickRoomMateMember(memberId: Int) {
        return RetrofitInstance.service.kickRoomMateMember(memberId)
    }


    /**
     * 최후의 남은 팀원 1명이 팀 폭발했음을 확인했을 때 해당 API를 요청해주세요.
     */
    suspend fun assumeTeamDisorganization() {
        return RetrofitInstance.service.assumeTeamDisorganization()
    }

}