package org.appcenter.inudorm.repository

import io.sentry.protocol.Response
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.OnbaordInfo
import org.appcenter.inudorm.networking.RetrofitInstance

/**
 * 이 Repository 는 본인의 매칭정보를 조회/수정/삭제/생성/공개여부변경이 가능합니다.
 */
class MatchingInfoRepository {
    suspend fun fetchMatchingInfo(): MatchingInfo {
        return RetrofitInstance.service.lookupMatchingInfo()
    }

    suspend fun saveMatchingInfo(params: OnbaordInfo): OnbaordInfo {
        return RetrofitInstance.service.saveMatchingInfo(params)
    }

}