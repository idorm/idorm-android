package org.appcenter.inudorm.usecase

import dagger.hilt.android.lifecycle.HiltViewModel
import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.repository.CommunityRepository
import javax.inject.Inject

class WritePost @Inject
constructor(private val communityRepository: CommunityRepository) :
    UseCase<PostEditDto, Post>() {
    override suspend fun onExecute(params: PostEditDto): Post {
        return communityRepository.createPost(null, params)
    }
}