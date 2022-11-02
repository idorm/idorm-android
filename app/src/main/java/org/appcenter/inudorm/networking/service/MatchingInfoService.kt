package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.networking.Data
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchingInfoService{
    /**
     * 온보딩 시에 최초로 입력한 매칭 정보를 저장하는 API
     * @param body MatchingInfo를 Serialize한 것의 RequestBody
     * @return data가 빈 Data
     */
    @POST("matchinginfo")
    suspend fun saveMatchingInfo(@Body body: RequestBody) : Data<Nothing>
}
