package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto


class WritePost : UseCase<PostEditDto, Post>() {
    override suspend fun onExecute(params: PostEditDto): Post {
        return communityRepository.createPost(params)
    }
}