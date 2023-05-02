package org.appcenter.inudorm.repository

import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.OnboardParams

/**
 * 이 Repository 는 본인의 매칭정보를 조회/수정/삭제/생성/공개여부변경이 가능합니다.
 */
class MatchingInfoRepository {
    suspend fun fetchMatchingInfo(): MatchingInfo {
        return RetrofitInstance.service.lookupMatchingInfo()
    }

    suspend fun saveMatchingInfo(params: OnboardParams): OnboardInfo {
        val str = gson.toJson(params.onboardInfo)
        return RetrofitInstance.service.saveMatchingInfo(createJsonRequestBody(str))
    }

    suspend fun editMatchingInfo(params: OnboardParams): OnboardInfo {
        val str = gson.toJson(params.onboardInfo)
        return RetrofitInstance.service.editMatchingInfo(createJsonRequestBody(str))
    }
}