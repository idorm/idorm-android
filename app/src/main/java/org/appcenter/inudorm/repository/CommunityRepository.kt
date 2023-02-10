package org.appcenter.inudorm.repository

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.ContentUriRequestBody
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.UserInputParams
import retrofit2.http.Body
import java.io.File

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

}