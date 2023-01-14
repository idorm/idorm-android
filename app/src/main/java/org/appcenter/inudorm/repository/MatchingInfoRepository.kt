package org.appcenter.inudorm.repository

import kotlinx.coroutines.delay
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.usecase.SetMatchingInfoVisibility

/**
 * 이 Repository 는 본인의 매칭정보를 조회/수정/삭제/생성/공개여부변경이 가능합니다.
 */
class MatchingInfoRepository {
    suspend fun fetchMatchingInfo(): MatchingInfo {
        return RetrofitInstance.service.lookupMatchingInfo()
    }
}