package org.appcenter.inudorm.networking.service

import okhttp3.RequestBody
import org.appcenter.inudorm.model.ChangeNickNameDto
import org.appcenter.inudorm.model.ChangePasswordDto
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Post
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Member

interface CommunityService {

    /**
     * @param dorm 조회하고자 하는 기숙사
     * @return Post
     */
    @GET("member/posts/{dorm}/top")
    suspend fun getTopPostsByDorm(@Path("dorm") dorm: Dorm): ArrayList<Post>

    /**
     * @param dorm 조회하고자 하는 기숙사
     * @return Post
     */
    @GET("member/posts/{dorm}")
    suspend fun getPostsByDorm(@Path("dorm") dorm: Dorm, @Query("page") page: Int): ArrayList<Post>

    /**
     * @param id 글 id
     * @return Post
     */
    @GET("member/post/{postId}")
    suspend fun getSinglePost(@Path("postId") id: Int): Post
}