package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.board.State

class DeleteComment : UseCase<DeleteComment.Param, State>() {
    data class Param(val postId: Int, val commentId: Int)

    override suspend fun onExecute(params: DeleteComment.Param): State {
        return kotlin.runCatching {
            State.Success(communityRepository.deleteComment(params.commentId, params.postId))
        }.getOrElse { State.Error(it) }
    }
}