package com.softwaremq.myzemogaapp.network

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext


suspend fun <T> safeApiCall(context: CoroutineContext, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(context) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.message()
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}
val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
    Log.d("coroutineExceptionHandler",throwable.localizedMessage ?: "Unknown error")
}
val backgroundCoroutineContext = Dispatchers.IO + coroutineExceptionHandler
val foregroundCoroutineContext = Dispatchers.Main + coroutineExceptionHandler