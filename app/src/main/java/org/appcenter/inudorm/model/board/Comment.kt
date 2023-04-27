package org.appcenter.inudorm.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId: Int,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val memberId: Int,
    val nickname: String,
    val parentCommentId: Int?,
    val profileUrl: String?,
    var subComments: ArrayList<Comment>?,
    val postId: Int,
) : Parcelable