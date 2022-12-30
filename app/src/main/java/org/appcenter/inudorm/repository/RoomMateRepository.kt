package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.service.ReqBody

class RoomMateRepository {
    suspend fun fetchRoomMates(params: RoomMateFilter): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.filterMatchingInfo(params)
    }
    suspend fun fetchLikedMatchingInfoList(): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.likedMatchingInfo()
    }
    suspend fun addDislikedMatchingInfo(id:Int) {
        return RetrofitInstance.service.addDislikedMatchingInfo(id)
    }
    suspend fun deleteDislikedMatchingInfo(id:Int) {
        return RetrofitInstance.service.deleteDislikeMatchingInfo(id)
    }
    suspend fun addLikedMatchingInfo(id:Int) {
        return RetrofitInstance.service.addLikedMatchingInfo(id)
    }
    suspend fun deleteLikedMatchingInfo(id:Int) {
        return RetrofitInstance.service.deleteLikeMatchingInfo(id)
    }


}