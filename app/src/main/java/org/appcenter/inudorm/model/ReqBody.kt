package org.appcenter.inudorm.model

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.ByteString
import okio.ByteString.Companion.encode
import org.appcenter.inudorm.App
import org.appcenter.inudorm.util.IDormLogger
import java.nio.charset.StandardCharsets

abstract class ReqBody() : RequestBody() {
    @Transient
    private var bodyByteString: ByteString? = null

    override fun contentType(): MediaType? = ("application/json; charset=utf-8").toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        var charset = StandardCharsets.UTF_8
        if (contentType() != null) {
            charset = contentType()!!.charset()
            if (charset == null) {
                charset = StandardCharsets.UTF_8
            }
        }
        bodyByteString = App.gson.toJson(this).encode(charset)
        IDormLogger.i(this, bodyByteString?.size.toString())
        sink.write(bodyByteString!!)
    }

    override fun contentLength(): Long {
        IDormLogger.i(this, bodyByteString?.size.toString())
        return bodyByteString?.size?.toLong() ?: 0;
    }
}

