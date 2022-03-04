package com.softwaremq.myzemogaapp.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.softwaremq.myzemogaapp.utils.singleArgViewModelFactory
import com.softwaremq.myzemogaapp.base.BaseRepository
import com.softwaremq.myzemogaapp.base.BaseViewModel
import com.softwaremq.myzemogaapp.models.CommentData
import com.softwaremq.myzemogaapp.models.UserData
import com.softwaremq.myzemogaapp.network.ResultWrapper
import com.softwaremq.myzemogaapp.network.backgroundCoroutineContext
import com.softwaremq.myzemogaapp.network.foregroundCoroutineContext
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.save
import com.vicpin.krealmextensions.saveAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailsViewModel (private val repository: BaseRepository) : BaseViewModel(repository){

    companion object {
        val FACTORY = singleArgViewModelFactory(::PostDetailsViewModel)
    }

    private val _comments = MutableLiveData<List<CommentData>>()
    val comments : LiveData<List<CommentData>>
        get() = _comments

    private val _user = MutableLiveData<List<UserData>>()
    val user : LiveData<List<UserData>>
        get() = _user


    fun loadFragment(userId: Int, postId: Int){
        viewModelScope.launch(backgroundCoroutineContext) {
            val responseUser = repository.getUser(userId)

            when(responseUser){
                is ResultWrapper.NetworkError -> {
                    showNetworkError()
                    launch(foregroundCoroutineContext){
                        _user.value = UserData().query { equalTo("id",userId) }
                    }
                }
                is ResultWrapper.GenericError -> responseUser.error?.let { showGenericError(it) }
                is ResultWrapper.Success -> responseUser.value.let {
                    launch(foregroundCoroutineContext){
                        if(it.isNotEmpty())
                            it[0].save()
                        _user.value = it
                    }
                }
            }

            val responseComments = repository.getCommentsByPost(postId)

            when(responseComments){
                is ResultWrapper.NetworkError -> {
                    showNetworkError()
                    launch(foregroundCoroutineContext){
                        _comments.value = CommentData().query { equalTo("postId",postId) }
                    }
                }
                is ResultWrapper.GenericError -> responseComments.error?.let { showGenericError(it) }
                is ResultWrapper.Success -> responseComments.value.let {
                    launch(foregroundCoroutineContext){
                        it.saveAll()
                        _comments.value = it
                    }
                }
            }
        }
    }

}