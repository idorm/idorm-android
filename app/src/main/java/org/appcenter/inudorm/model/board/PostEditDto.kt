package org.appcenter.inudorm.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.appcenter.inudorm.model.Dorm
import java.io.File


@Parcelize
data class PostEditDto(
    val title: String,
    val content: String,
    val dormCategory: Dorm?,
    val isAnonymous: Boolean,
    val files: List<File>? = null,
    val deletePostPhotoIds: ArrayList<Int>? = null
) : Parcelable {
    fun toFormData(): HashMap<String, RequestBody> {
        val reqMap = HashMap<String, RequestBody>()
        for (field in this::class.java.declaredFields) {
            if (field.get(this) != null && field.name != "files")
                reqMap[field.name] =
                    field.get(this)?.toString()!!.toRequestBody("text/plain".toMediaTypeOrNull())
        }
        return reqMap
    }

}