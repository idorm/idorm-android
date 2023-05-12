package org.appcenter.inudorm.model.board

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import io.sentry.Sentry
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import org.appcenter.inudorm.model.Dorm
import java.lang.reflect.Type

@Parcelize
data class Post(
    var comments: ArrayList<Comment>? = null,
    val commentsCount: Int = 0,
    val content: String,
    val createdAt: String,
    val imagesCount: Int = 0,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val postPhotos: ArrayList<Photo>? = null,
    val postId: Int,
    val profileUrl: String? = null,
    val title: String,
    val updatedAt: String? = null,
    val memberId: Int,
    val dormCategory: Dorm,
    val nickname: String?,
    var isWithdrew: Boolean = false,
    var isAnonymous: Boolean = false,
) : Parcelable {
    companion object {
        val NICKNAME_DESERIALIZER =
            JsonDeserializer { json, typeOfT, context ->
                val jsonObject = json?.asJsonObject ?: return@JsonDeserializer null
                val nicknameJsonElement = jsonObject.get("nickname")
                if (nicknameJsonElement == null || nicknameJsonElement.isJsonNull || nicknameJsonElement.asString.isNullOrEmpty())
                    jsonObject.addProperty("nickname", "탈퇴한 회원")
                return@JsonDeserializer Gson().fromJson(jsonObject, Post::class.java)
            }
    }

    fun isCommentEmpty(): Boolean {
        if (comments == null) return true
        // 하나라도 deleted가 false 이거나 subComment가 null이 아닌 걸 찾으면
        for (comment in comments!!) {
            // 대댓글이 있으면 거기서 isDeleted 가 false 인 걸 찾아서 하나라도 있으면 비어있지 않음.
            if (!comment.isDeleted) return false
            if (comment.subComments != null)
                for (subComment in comment.subComments!!) {
                    if (subComment.isDeleted) break else return false
                }
            // 대댓글이 빈걸 못 찾으면
        }
        return true
    }
}