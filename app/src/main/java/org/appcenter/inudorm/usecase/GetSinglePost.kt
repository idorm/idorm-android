package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.board.Post

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetSinglePost() : UseCase<Int, Post>() {
    override suspend fun onExecute(params: Int): Post {
        return communityRepository.getSinglePost(params)
    }
}