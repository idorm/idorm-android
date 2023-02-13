package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.board.State

class DeletePost : UseCase<Int, State>() {
    override suspend fun onExecute(params: Int): State {
        return kotlin.runCatching {
            State.Success(communityRepository.deletePost(params))
        }.getOrElse { State.Error(it) }
    }
}