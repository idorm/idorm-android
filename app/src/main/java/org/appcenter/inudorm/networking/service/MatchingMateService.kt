package org.appcenter.inudorm.networking.service

import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import retrofit2.http.*

interface MatchingMateService {
    /**
     * 매칭 정보를 검색 조건을 걸어 가져오는 API
     * @parma body
     */
    @POST("/member/matching/filter")
    suspend fun filterMatchingInfo(@Body body: RoomMateFilter): ArrayList<MatchingInfo>

    @POST("/member/matching/like")
    suspend fun likedMatchingInfo(): ArrayList<MatchingInfo>

    @POST("/member/matching/{id}")
    suspend fun addLikedOrDislikedMatchingInfo(
        @Path("id") id: Int,
        @Query("matchingType") matchingType: Boolean,
    )

    @DELETE("/member/matching/{id}")
    suspend fun deleteLikeOrDislikeMatchingInfo(
        @Path("id") id: Int,
        @Query("matchingType") matchingType: Boolean,
    )

    @GET("/member/matching/like")
    suspend fun getLikedMatchingMates(): ArrayList<MatchingInfo>

    @GET("/member/matching/dislike")
    suspend fun getDisLikedMatchingMates(): ArrayList<MatchingInfo>

}
