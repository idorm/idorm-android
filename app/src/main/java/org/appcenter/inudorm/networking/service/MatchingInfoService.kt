package org.appcenter.inudorm.networking.service

import io.sentry.protocol.Response
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.OnbaordInfo
import retrofit2.http.*


interface MatchingInfoService {

    /**
     * 현재 로그인한 사용자의 매칭정보를 조회하는 API
     */
    @GET("/member/matchinginfo")
    suspend fun lookupMatchingInfo(): MatchingInfo

    /**
     * 온보딩 시에 최초로 입력한 매칭 정보를 저장하는 API
     * @param body MatchingInfo를 Serialize한 것의 RequestBody
     * @return data가 빈 Data
     */
    suspend fun saveMatchingInfo(@Body body: OnbaordInfo): OnbaordInfo

    @PATCH("member/matchinginfo")
    suspend fun setMatchingInfoVisibility(@Query("isMatchingInfoPublic") visibility: Boolean)

}
