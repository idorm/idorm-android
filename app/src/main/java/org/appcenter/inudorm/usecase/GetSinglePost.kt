package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetSinglePost() : UseCase<Int, Post>() {
    override suspend fun onExecute(params: Int): Post {
        val post = communityRepository.getSinglePost(params)
        val comments = post.comments?.groupBy {
            it.parentCommentId
        }
        val parents = comments?.entries?.filter {
            it.key == null
        }?.first()?.value!!
        val children = comments.entries?.filter {
            it.key != null
        }
        post.comments = ArrayList().addAll()parents.map { parent ->
            val subComments = arrayListOf<Comment>()
            subComments.addAll(children?.filter {
                it.key == parent.commentId
            }?.first()?.value!!)
            Comment(
                commentId = parent.commentId,
                content = parent.content,
                createdAt = parent.createdAt,
                isDeleted = parent.isDeleted,
                nickname = parent.nickname,
                parentCommentId = parent.parentCommentId,
                profileUrl = parent.profileUrl,
                subComments = subComments
            )
        }


    }
}