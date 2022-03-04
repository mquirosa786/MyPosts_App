package com.softwaremq.myzemogaapp.base

import com.softwaremq.myzemogaapp.models.CommentData
import com.softwaremq.myzemogaapp.models.PostData
import com.softwaremq.myzemogaapp.models.UserData
import com.softwaremq.myzemogaapp.network.ApiServices
import com.softwaremq.myzemogaapp.network.ResultWrapper
import com.softwaremq.myzemogaapp.network.backgroundCoroutineContext
import com.softwaremq.myzemogaapp.network.safeApiCall

class BaseRepository (val api : ApiServices) {


    suspend fun getPosts(): ResultWrapper<List<PostData>> {
        return safeApiCall(backgroundCoroutineContext) { api.getPosts()}
    }

    suspend fun deletePost(postId: Int): ResultWrapper<Any> {
        return safeApiCall(backgroundCoroutineContext) { api.deletePost(postId)}
    }

    suspend fun getCommentsByPost(postId: Int): ResultWrapper<List<CommentData>> {
        return safeApiCall(backgroundCoroutineContext) { api.getCommentsByPost(postId)}
    }

    suspend fun getUser(id: Int): ResultWrapper<List<UserData>> {
        return safeApiCall(backgroundCoroutineContext) { api.getUser(id)}
    }

}