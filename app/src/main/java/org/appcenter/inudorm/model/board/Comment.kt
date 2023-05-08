package org.appcenter.inudorm.model.board

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId: Int,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val memberId: Int,
    val nickname: String?,
    val parentCommentId: Int?,
    val profileUrl: String?,
    var subComments: ArrayList<Comment>?,
    val postId: Int,
) : Parcelable {
    companion object {
        val NICKNAME_DESERIALIZER =
            JsonDeserializer { json, typeOfT, context ->
                val jsonObject = json?.asJsonObject ?: return@JsonDeserializer null
                val nicknameJsonElement = jsonObject.get("nickname")
                if (nicknameJsonElement == null || nicknameJsonElement.isJsonNull)
                    jsonObject.addProperty("nickname", "탈퇴한 회원")
                return@JsonDeserializer Gson().fromJson(jsonObject, Comment::class.java)
            }
    }
}