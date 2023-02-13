package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.presentation.board.State

class ToggleLikePost : UseCase<ToggleLikePost.Param, State>() {
    data class Param(val postId: Int, val liked: Boolean)

    override suspend fun onExecute(params: Param): State {
        return kotlin.runCatching {
            val result =
                if (params.liked) communityRepository.deletePostLike(params.postId)
                else communityRepository.likePost(params.postId)
            State.Success(result)
        }.getOrElse { State.Error(it) }
    }
}