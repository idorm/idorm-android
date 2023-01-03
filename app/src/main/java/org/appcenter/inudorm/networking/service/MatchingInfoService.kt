package org.appcenter.inudorm.networking.service

import androidx.annotation.Nullable
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.ByteString
import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.util.IDormLogger
import retrofit2.http.*
import java.nio.charset.StandardCharsets

abstract class ReqBody() : RequestBody() {
    @Transient
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
        bodyByteString = ByteString.encodeString(gson.toJson(this), charset)
        IDormLogger.i(this, bodyByteString?.size().toString())
        sink.write(bodyByteString!!)
    }

    override fun contentLength(): Long {
        IDormLogger.i(this, bodyByteString?.size().toString())
        return bodyByteString?.size()?.toLong() ?: 0;
    }
}

interface MatchingInfoService {
    /**
     * 매칭 정보를 검색 조건을 걸어 가져오는 API
     * @parma body
     */
    @POST("/member/matching/filter")
    suspend fun filterMatchingInfo(@Body body: RoomMateFilter): ArrayList<MatchingInfo>

    @POST("/member/matching/like")
    suspend fun likedMatchingInfo() : ArrayList<MatchingInfo>

    @POST("/member/matching/dislike/{id}")
    suspend fun addDislikedMatchingInfo(@Path("id") id:Int)

    @DELETE("/member/matching/dislike/{id}")
    suspend fun deleteDislikeMatchingInfo(@Path("id") id:Int)

    @POST("/member/matching/like/{id}")
    suspend fun addLikedMatchingInfo(@Path("id") id:Int)

    @DELETE("/member/matching/like/{id}")
    suspend fun deleteLikeMatchingInfo(@Path("id") id:Int)

    /**
     * 온보딩 시에 최초로 입력한 매칭 정보를 저장하는 API
     * @param body MatchingInfo를 Serialize한 것의 RequestBody
     * @return data가 빈 Data
     */
    @POST("matchinginfo")
    suspend fun saveMatchingInfo(@Body body: RequestBody): Nothing
}
