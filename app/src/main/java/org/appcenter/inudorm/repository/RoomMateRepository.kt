package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.service.ReqBody

class RoomMateRepository {
    suspend fun fetchRoomMates(params: RoomMateFilter): ArrayList<MatchingInfo> {
        return RetrofitInstance.service.filterMatchingInfo(ReqBody(params))
    }
}