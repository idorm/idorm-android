package org.appcenter.inudorm.networking.service

import androidx.annotation.Nullable
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.ByteString
import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.MatchingInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.nio.charset.StandardCharsets

class ReqBody(content: Any) : RequestBody() {
    private val bodyString: String = gson.toJson(content)
    private var bodyByteString: ByteString? = null

    override fun contentType(): MediaType? = MediaType.parse(("application/json; charset=utf-8"))

    override fun writeTo(sink: BufferedSink) {
        var charset = StandardCharsets.UTF_8
        if (contentType() != null) {
            charset = contentType()!!.charset()
            if (charset == null) {
                charset = StandardCharsets.UTF_8
            }
        }
        bodyByteString = ByteString.encodeString(bodyString, charset)
        sink.write(bodyByteString!!)
    }

    override fun contentLength(): Long {
        return bodyByteString?.size()?.toLong() ?: 0;
    }
}

interface MatchingInfoService {
    /**
     * 매칭 정보를 검색 조건을 걸어 가져오는 API
     */
    @POST("/member/matching/filter")
    suspend fun filterMatchingInfo(@Body body: ReqBody): ArrayList<MatchingInfo>

    /**
     * 온보딩 시에 최초로 입력한 매칭 정보를 저장하는 API
     * @param body MatchingInfo를 Serialize한 것의 RequestBody
     * @return data가 빈 Data
     */
    @POST("matchinginfo")
    suspend fun saveMatchingInfo(@Body body: RequestBody): Nothing
}
