package org.appcenter.inudorm.model

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ActivityContext
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import javax.inject.Inject


class ContentUriRequestBody(
    context: Context,
    private val uri: Uri,
) :
    RequestBody() {

    private val contentResolver = context.contentResolver

    private var fileName = ""
    private var size = -1L

    init {
        contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                fileName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            }
        }
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? = MediaType.parse(contentResolver.getType(uri)!!)

    override fun writeTo(sink: BufferedSink) {
        val stream = contentResolver.openInputStream(uri)
        sink.write(stream?.readBytes()!!)
        stream.close()
    }

}