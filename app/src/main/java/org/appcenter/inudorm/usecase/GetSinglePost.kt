package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.util.IDormLogger

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetSinglePost : UseCase<Int, Post>() {
    override suspend fun onExecute(params: Int): Post {
        val post = communityRepository.getSinglePost(params)

        return if (post.comments != null) {
            // 부모 있는거 / 없는거로 분리
            val groupedComments = post.comments?.groupBy {
                it.parentCommentId
            }
            val parents = ArrayList<Comment>()
            val children = groupedComments?.filter { it.key != null }

            // 부모 id가 없는 (1 depth)를 parents에 추가
            parents.addAll(groupedComments?.get(null) ?: ArrayList())
            children?.forEach {
                // parent의 commentId와 일치하게 grouping 된 리스트를 붙이기
                parents.forEach { parent ->
                    if (it.key == parent.commentId) {
                        if (parent.subComments == null)
                            parent.subComments = ArrayList()
                        parent.subComments?.addAll(it.value)
                        if ((parent.subComments?.size ?: 0) > 0)
                            parent.subComments?.sortBy { it.createdAt }
                    }
                }
            }
            post.copy(comments = parents)
        } else {
            post
        }


    }
}