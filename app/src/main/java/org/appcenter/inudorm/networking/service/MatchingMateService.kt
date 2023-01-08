package org.appcenter.inudorm.networking.service

import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface MatchingMateService {
    /**
     * 매칭 정보를 검색 조건을 걸어 가져오는 API
     * @parma body
     */
    @POST("/member/matching/filter")
    suspend fun filterMatchingInfo(@Body body: RoomMateFilter): ArrayList<MatchingInfo>

    @POST("/member/matching/like")
    suspend fun likedMatchingInfo(): ArrayList<MatchingInfo>

    @POST("/member/matching/dislike/{id}")
    suspend fun addDislikedMatchingInfo(@Path("id") id: Int)

    @DELETE("/member/matching/dislike/{id}")
    suspend fun deleteDislikeMatchingInfo(@Path("id") id: Int)

    @POST("/member/matching/like/{id}")
    suspend fun addLikedMatchingInfo(@Path("id") id: Int)

    @DELETE("/member/matching/like/{id}")
    suspend fun deleteLikeMatchingInfo(@Path("id") id: Int)

}
