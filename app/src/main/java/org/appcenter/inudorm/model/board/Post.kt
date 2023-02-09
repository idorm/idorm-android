package org.appcenter.inudorm.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var comments: ArrayList<Comment>? = null,
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
    val updatedAt: String? = null,
) : Parcelable