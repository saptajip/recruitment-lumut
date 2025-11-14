package com.example.saptajiprasetyo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saptajiprasetyo.data.model.Post
import com.example.saptajiprasetyo.data.repository.PostRepository
import com.example.saptajiprasetyo.utils.Resource
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: PostRepository = PostRepository()) : ViewModel() {

    // LiveData untuk menyimpan hasil detail post (membungkus status Resource)
    private val _postDetail = MutableLiveData<Resource<Post>>()
    val postDetail: LiveData<Resource<Post>> = _postDetail

    fun fetchPostDetail(postId: Int) {
        // Jangan fetch jika sudah ada data
        if (_postDetail.value != null && _postDetail.value is Resource.Success) return

        viewModelScope.launch {
            _postDetail.postValue(Resource.Loading())
            try {
                val response = repository.getPostDetail(postId)
                if (response.isSuccessful && response.body() != null) {
                    _postDetail.postValue(Resource.Success(response.body()!!))
                } else {
                    _postDetail.postValue(Resource.Error("Gagal mengambil detail: ${response.code()}"))
                }
            } catch (e: Exception) {
                _postDetail.postValue(Resource.Error(e.message ?: "Kesalahan Jaringan"))
            }
        }
    }
}