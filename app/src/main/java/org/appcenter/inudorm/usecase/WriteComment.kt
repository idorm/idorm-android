package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.board.State


class WriteComment : UseCase<WriteCommentDto, State>() {
    override suspend fun onExecute(params: WriteCommentDto): State {
        return kotlin.runCatching {
            State.Success(
                communityRepository.registerComment(
                    params.postId ?: throw IDormError(ErrorCode.INVALID_REQEUST_PARAM), params
                )
            )
        }.getOrElse {
            State.Error(it)
        }
    }

}