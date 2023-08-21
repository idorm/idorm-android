package org.appcenter.inudorm.util

import android.content.Context
import android.database.Cursor
import android.net.Uri


object ImageUri {
    const val defaultProfileImage =
        "https://idorm-static.s3.ap-northeast-2.amazonaws.com/profileImage.png"
    const val defaultThumbnailImage =
        "https://idorm-static.s3.ap-northeast-2.amazonaws.com/nadomi.png"
    fun getFullPathFromUri(ctx: Context, uri: Uri): String? {
        val cursor: Cursor = ctx.contentResolver.query(uri, null, null, null, null) ?: return null
        cursor.moveToNext()
        val path: String = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
        cursor.close()
        IDormLogger.i(this, uri.toString())
        return path
    }
}