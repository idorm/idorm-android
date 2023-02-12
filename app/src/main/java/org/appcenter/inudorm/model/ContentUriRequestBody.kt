package org.appcenter.inudorm.model

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio


class ContentUriRequestBody(val context: Context, val uri: Uri) : RequestBody() {
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
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val fileNameIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                size = cursor.getLong(sizeIndex)
                fileName = cursor.getString(fileNameIndex)
            }
        }
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? = MediaType.parse(contentResolver.getType(uri) ?: throw java.lang.RuntimeException("미디어타입 파싱 실패"))

    override fun writeTo(sink: BufferedSink) {
        Okio.source(
            contentResolver.openInputStream(uri)
                ?: throw IllegalStateException("Couldn't open content URI for reading: $uri")
        ).use { source ->

            sink.writeAll(source)
        }
    }

/*    private fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path!!.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor: Cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        ) ?: return null
        cursor.use {
            val columnIndex: Int = it.getColumnIndex(columns[0])
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }

    override fun contentType(): MediaType? {
        val contentType = context.contentResolver.getType(uri) ?: return null
        return MediaType.parse(contentType)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeTo(sink: BufferedSink) {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Couldn't open content URI for reading: $uri")
        val bytes = inputStream.readBytes()
        Okio.source(inputStream).use { source ->

            sink.writeAll(source)
        }
    }*/

}