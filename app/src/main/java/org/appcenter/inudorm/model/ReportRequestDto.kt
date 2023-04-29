package org.appcenter.inudorm.model

import org.appcenter.inudorm.presentation.board.Content

data class ReportRequestDto(
    val memberOrPostOrCommentId: Int,
    val reason: String,
    val reasonType: String,
    val reportType: Content,
) : ReqBody()