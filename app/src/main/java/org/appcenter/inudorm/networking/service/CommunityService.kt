package org.appcenter.inudorm.networking.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.WriteCommentDto
import retrofit2.http.*

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

    @Multipart
    @POST("member/post")
    suspend fun createPost(
        @PartMap body: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part>,
    ): Post

    /**
     * @param id 글 id
     * @return Post
     */
    @POST("member/post/{postId}")
    suspend fun updatePost(
        @Path("postId") id: Int,
        @PartMap body: HashMap<String, RequestBody>,
//        @Part files: List<ContentUriRequestBody>,
    ): Post

    @POST("member/post/{post-id}/comment")
    suspend fun registerComment(
        @Path("post-id") postId: Int,
        @Body commentDto: WriteCommentDto,
    ): Comment

    @DELETE("member/post/{post-id}")
    suspend fun deletePost(
        @Path("post-id") postId: Int,
    )

    @DELETE("member/post/{post-id}/comment/{comment-id}")
    suspend fun deleteComment(
        @Path("post-id") postId: Int,
        @Path("comment-id") commentId: Int,
    )

    @PUT("member/post/{post-id}/like")
    suspend fun likePost(
        @Path("post-id") postId: Int,
    )

    @DELETE("member/post/{post-id}/like")
    suspend fun deletePostLike(
        @Path("post-id") postId: Int,
    )




}