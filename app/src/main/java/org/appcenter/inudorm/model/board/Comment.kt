package org.appcenter.inudorm.model.board

data class Comment(
    val commentId: Int,
    val content: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val nickname: String,
    val parentCommentId: Int?,
    val profileUrl: String,
    val subComments: ArrayList<Comment>,
)