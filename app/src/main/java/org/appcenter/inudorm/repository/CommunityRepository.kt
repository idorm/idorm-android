package org.appcenter.inudorm.repository

import android.content.Context
import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class CommunityRepository @Inject constructor() {
    suspend fun getTopPostsByDorm(dorm: Dorm, page: Int): ArrayList<Post> {
        return RetrofitInstance.service.getTopPostsByDorm(dorm)
    }

    suspend fun getPostsByDorm(dorm: Dorm, page: Int): ArrayList<Post> {
        return RetrofitInstance.service.getPostsByDorm(dorm, page)
    }

    suspend fun getSinglePost(id: Int): Post {
        return RetrofitInstance.service.getSinglePost(id)
    }

    @Provides
    @Singleton
    suspend fun createPost(
        @ApplicationContext
        context: Context? = null,
        post: PostEditDto,
    ): Post {
        return RetrofitInstance.service.createPost(
            post.toFormData(), post.getUploadableImages(context!!)
        )
    }

    suspend fun updatePost(id: Int, post: PostEditDto): Post {
        return RetrofitInstance.service.updatePost(id, post.toFormData())
    }

}