package org.appcenter.inudorm.model.board

data class Post(
    val comments: ArrayList<Comment>? = null,
    val commentsCount: Int = 0,
    val content: String,
    val createdAt: String,
    val imagesCount: Int = 0,
    val likesCount: Int = 0,
    val nickname: String,
    val photoUrls: ArrayList<String>? = null,
    val postId: Int,
    val profileUrl: String? = null,
    val title: String,
    val updatedAt: String? = null
)