package org.appcenter.inudorm.networking.service

import androidx.annotation.Nullable
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.ByteString
import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.SetMatchingInfoVisibilityDto
import org.appcenter.inudorm.util.IDormLogger
import retrofit2.http.*
import java.nio.charset.StandardCharsets


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
    @POST("/member/matchinginfo")
    suspend fun saveMatchingInfo(@Body body: RequestBody): Nothing

    @PATCH("member/matchinginfo?isMatchingInfoPublic={visibility}")
    suspend fun setMatchingInfoVisibility(@Query("visibility") visibility: Boolean)

}
