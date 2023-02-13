package org.appcenter.inudorm.repository

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.networking.RetrofitInstance

class CommunityRepository {
    suspend fun getTopPostsByDorm(dorm: Dorm, page: Int): ArrayList<Post> {
        return RetrofitInstance.service.getTopPostsByDorm(dorm)
    }

    suspend fun getPostsByDorm(dorm: Dorm, page: Int): ArrayList<Post> {
        return RetrofitInstance.service.getPostsByDorm(dorm, page)
    }

    suspend fun getSinglePost(id: Int): Post {
        return RetrofitInstance.service.getSinglePost(id)
    }

    suspend fun createPost(post: PostEditDto): Post {
        val files = ArrayList<MultipartBody.Part>()
        post.files?.forEach {
            val requestFile = RequestBody.create(MediaType.parse("image/jpg"), it)
            files.add(MultipartBody.Part.createFormData("files", it.name, requestFile))
        }
        return RetrofitInstance.service.createPost(post.toFormData(), files)
    }

    suspend fun updatePost(id: Int, post: PostEditDto): Post {
        return RetrofitInstance.service.updatePost(id, post.toFormData())
    }

    suspend fun registerComment(postId: Int, commentDto: WriteCommentDto): Comment {
        return RetrofitInstance.service.registerComment(postId, commentDto)
    }

    suspend fun deletePost(postId: Int) {
        return RetrofitInstance.service.deletePost(postId)
    }

    suspend fun reportPost(postId: Int) {
//        return RetrofitInstance.service.reportPost(postId)
    }
    suspend fun likePost(postId: Int) {
        return RetrofitInstance.service.likePost(postId)
    }

    suspend fun deletePostLike(postId: Int) {
        return RetrofitInstance.service.deletePostLike(postId)
    }


    suspend fun deleteComment(commentId: Int, postId: Int) {
        return RetrofitInstance.service.deleteComment(commentId, postId)
    }

    suspend fun reportComment(commentId: Int) {
//        return RetrofitInstance.service.reportComment(commentId)
    }


}