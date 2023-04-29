package org.appcenter.inudorm.repository

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.networking.RetrofitInstance
import retrofit2.Retrofit
import retrofit2.http.GET

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
            val requestFile = it.asRequestBody("image/jpg".toMediaTypeOrNull())
            files.add(MultipartBody.Part.createFormData("files", it.name, requestFile))
        }
        return RetrofitInstance.service.createPost(post.toFormData(), files)
    }

    suspend fun updatePost(id: Int, post: PostEditDto): Post {
        val files = ArrayList<MultipartBody.Part>()
        post.files?.forEach {
            val requestFile = it.asRequestBody("image/jpg".toMediaTypeOrNull())
            files.add(MultipartBody.Part.createFormData("files", it.name, requestFile))
        }
        val parts = post.deletePostPhotoIds?.map {
            MultipartBody.Part.createFormData(
                "deletePostPhotoIds",
                it.toString()
            )
        }
        return RetrofitInstance.service.updatePost(
            id,
            post.toFormData(),
            files,
            parts ?: listOf()
        )
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



    suspend fun getWrotePosts(): ArrayList<Post> {
        return RetrofitInstance.service.getWrotePosts()
    }

    suspend fun getLikedPosts(): ArrayList<Post> {
        return RetrofitInstance.service.getLikedPosts()
    }

    suspend fun getWroteComments(): ArrayList<Comment> {
        return RetrofitInstance.service.getWroteComments()
    }


}