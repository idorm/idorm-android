package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.repository.CommunityRepository
import javax.inject.Inject

data class PostUpdateParams(val id: Int, val request: PostEditDto)

class UpdatePost @Inject
constructor(private val communityRepository: CommunityRepository) :
    UseCase<PostUpdateParams, Post>() {
    override suspend fun onExecute(params: PostUpdateParams): Post {
        return communityRepository.updatePost(params.id, params.request)
    }
}