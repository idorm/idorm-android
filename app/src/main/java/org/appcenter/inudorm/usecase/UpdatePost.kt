package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto

data class PostUpdateParams(val id: Int, val request: PostEditDto)

class UpdatePost : UseCase<PostUpdateParams, Post>() {
    override suspend fun onExecute(params: PostUpdateParams): Post {
        return communityRepository.updatePost(params.id, params.request)
    }
}