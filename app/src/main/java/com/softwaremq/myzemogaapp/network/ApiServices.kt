package com.softwaremq.myzemogaapp.network

import androidx.lifecycle.LiveData
import com.softwaremq.myzemogaapp.models.CommentData
import com.softwaremq.myzemogaapp.models.PostData
import com.softwaremq.myzemogaapp.models.UserData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("posts")
    suspend fun getPosts() : List<PostData>

    @DELETE("posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ) : Any

    @GET("comments")
    suspend fun getCommentsByPost(
        @Query("postId") postId : Int,
    ) : List<CommentData>

    @GET("users")
    suspend fun getUser(
        @Query("id") id : Int,
    ) : List<UserData>

}