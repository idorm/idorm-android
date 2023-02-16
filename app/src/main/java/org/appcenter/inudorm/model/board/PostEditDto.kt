package org.appcenter.inudorm.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType
import okhttp3.RequestBody
import org.appcenter.inudorm.model.Dorm
import java.io.File


@Parcelize
data class PostEditDto(
    val title: String,
    val content: String,
    val dormCategory: Dorm?,
    val isAnonymous: Boolean,
    val files: List<File>? = null,
) : Parcelable {
    fun toFormData(): HashMap<String, RequestBody> {
        val reqMap = HashMap<String, RequestBody>()
        for (field in this::class.java.declaredFields) {
            if (field.get(this) != null && field.name != "files")
                reqMap[field.name] =
                    RequestBody.create(MediaType.parse("text/plain"), field.get(this)?.toString()!!)
        }
        return reqMap
    }

}