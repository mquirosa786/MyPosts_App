package com.softwaremq.myzemogaapp.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwaremq.myzemogaapp.network.ResultWrapper
import com.softwaremq.myzemogaapp.network.foregroundCoroutineContext
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext

open class BaseViewModel (private val repository: BaseRepository) : ViewModel(){
    val errorMessage = MutableLiveData<String>()

    protected fun onError(message: String) {
        viewModelScope.launch(foregroundCoroutineContext){
            errorMessage.value = message
            Log.d("BaseViewModelOnError",message)
        }
    }

    protected fun showNetworkError(message: String = "Network Error") {
        viewModelScope.launch(foregroundCoroutineContext){
            errorMessage.value = message
            Log.d("showNetworkError",message)
        }
    }

    protected fun showSuccess(message: String) {
        viewModelScope.launch(foregroundCoroutineContext){
            errorMessage.value = message
            Log.d("showSuccess",message)
        }
    }

    protected fun showGenericError(message: String) {
        viewModelScope.launch(foregroundCoroutineContext){
            errorMessage.value = message
            Log.d("showGenericError",message)
        }
    }

}