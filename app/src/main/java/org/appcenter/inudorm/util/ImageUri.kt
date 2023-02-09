package org.appcenter.inudorm.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


object ImageUri {
    fun getFullPathFromUri(ctx: Context, fileUri: Uri): String? {
        var fullPath: String? = null
        val column = "_data"
        var cursor: Cursor? = ctx.getContentResolver().query(fileUri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            var documentId: String = cursor.getString(0)
            documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
            cursor.close()
            val projection = arrayOf(column)
            try {
                cursor = ctx.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Images.Media._ID + " = ? ",
                    arrayOf(documentId),
                    null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    fullPath = cursor.getString(cursor.getColumnIndexOrThrow(column))
                }
            } finally {
                cursor.close()
            }
        }
        return fullPath
    }
}