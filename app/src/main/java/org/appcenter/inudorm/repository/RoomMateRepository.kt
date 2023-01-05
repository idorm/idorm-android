package org.appcenter.inudorm.repository

import kotlinx.coroutines.delay
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.SetMatchingInfoVisibilityDto
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.service.ReqBody
import org.appcenter.inudorm.usecase.SetMatchingInfoVisibility

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
    suspend fun reportMatchingInfo(id: Int) {
        return delay(2000L)
    }

    suspend fun setMatchingInfoVisibility(params: SetMatchingInfoVisibilityDto) {
        return RetrofitInstance.service.setMatchingInfoVisibility(params)
    }


}