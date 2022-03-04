package com.softwaremq.myzemogaapp.posts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.softwaremq.myzemogaapp.utils.singleArgViewModelFactory
import com.softwaremq.myzemogaapp.base.BaseRepository
import com.softwaremq.myzemogaapp.base.BaseViewModel
import com.softwaremq.myzemogaapp.models.PostData
import com.softwaremq.myzemogaapp.network.ResultWrapper
import com.softwaremq.myzemogaapp.network.backgroundCoroutineContext
import com.softwaremq.myzemogaapp.network.foregroundCoroutineContext
import com.vicpin.krealmextensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Response

class HomeViewModel (private val repository: BaseRepository) : BaseViewModel(repository){

    companion object {
        val FACTORY = singleArgViewModelFactory(::HomeViewModel)
    }


    val posts = MutableLiveData<List<PostData>>()


    fun callPosts(){
        viewModelScope.launch(backgroundCoroutineContext) {
            val response = repository.getPosts()

            when(response){
                is ResultWrapper.NetworkError -> {
                    showNetworkError()
                    launch(foregroundCoroutineContext){
                        posts.postValue(PostData().queryAll())
                    }
                }
                is ResultWrapper.GenericError -> response.error?.let { showGenericError(it) }
                is ResultWrapper.Success -> response.value.let {
                    launch(backgroundCoroutineContext){
                        val savedPosts = PostData().queryAll()
                        if(!savedPosts.isNullOrEmpty()){
                            it.forEach { responseList ->
                                PostData().queryFirst { equalTo("id",responseList.id) }?.let {
                                    responseList.favorite = it.favorite
                                }
                            }
                        }
                        val orderedList = it.sortedByDescending { it.favorite }
                        orderedList.saveAll()
                        launch(foregroundCoroutineContext){
                            posts.postValue(orderedList)
                        }
                    }
                }
            }

        }
    }

    fun deletePost(postId: Int){
        viewModelScope.launch(backgroundCoroutineContext) {

            PostData().delete { equalTo("id",postId) }
            val response = repository.deletePost(postId = postId)

            when(response){
                is ResultWrapper.NetworkError -> showNetworkError()
                is ResultWrapper.GenericError -> response.error?.let { showGenericError(it) }
                is ResultWrapper.Success -> {}
            }
        }
    }

    fun deleteAllPosts(deleteCache: Boolean){
        viewModelScope.launch(backgroundCoroutineContext){
            if (deleteCache)
                PostData().deleteAll()
            posts.postValue(emptyList())
        }
    }

}