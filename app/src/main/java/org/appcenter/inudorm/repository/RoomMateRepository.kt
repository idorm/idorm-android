package org.appcenter.inudorm.repository

import kotlinx.coroutines.delay
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.usecase.MutateFavoriteRequestDto

class RoomMateRepository {
    suspend fun fetchRoomMates(params: RoomMateFilter): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.filterMatchingInfo(params)
    }

    suspend fun fetchLikedMatchingInfoList(): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.likedMatchingInfo()
    }

    suspend fun addLikedOrDislikedMatchingInfo(params: MutateFavoriteRequestDto) {
        return RetrofitInstance.service.addLikedOrDislikedMatchingInfo(params.id, params.isLiked)
    }

    suspend fun deleteLikeOrDislikeMatchingInfo(params: MutateFavoriteRequestDto) {
        return RetrofitInstance.service.deleteLikeOrDislikeMatchingInfo(params.id, params.isLiked)
    }

    suspend fun reportMatchingInfo(id: Int) {
        return delay(2000L)
    }

    suspend fun setMatchingInfoVisibility(visibility: Boolean) {
        return RetrofitInstance.service.setMatchingInfoVisibility(visibility)
    }

    suspend fun getLikedMatchingMates(): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.getLikedMatchingMates()
    }

    suspend fun getDisLikedMatchingMates(): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.getDisLikedMatchingMates()
    }

}