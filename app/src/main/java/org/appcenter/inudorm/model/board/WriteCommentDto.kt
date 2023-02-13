package org.appcenter.inudorm.model.board

import org.appcenter.inudorm.model.ReqBody

data class WriteCommentDto(
    val postId: Int? = null,
    val parentCommentId: Int? = null,
    val isAnonymous: Boolean = true,
    val content: String = "",
) : ReqBody()
