package com.example.saptajiprasetyo.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saptajiprasetyo.data.model.Post
import com.example.saptajiprasetyo.data.repository.PostRepository
import com.example.saptajiprasetyo.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class PostViewModel(private val repository: PostRepository = PostRepository()): ViewModel() {
    val posts = MutableLiveData<Resource<List<Post>>>()

    fun fetchPosts(){
        viewModelScope.launch {
            posts.postValue(Resource.Loading())
            try {
                val response = repository.getPost()
                if (response.isSuccessful){
                    posts.postValue(Resource.Success(response.body()!!))
                } else {
                    posts.postValue(Resource.Error("Error: ${response.code()}"))
                }
            } catch (e: Exception){
                posts.postValue(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }
}