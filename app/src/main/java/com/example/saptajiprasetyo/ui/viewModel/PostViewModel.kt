package com.example.saptajiprasetyo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.saptajiprasetyo.data.model.Post
import com.example.saptajiprasetyo.data.repository.PostRepository
import com.example.saptajiprasetyo.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class PostViewModel(private val repository: PostRepository = PostRepository()): ViewModel() {
    val posts = MutableLiveData<Resource<List<Post>>>()

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _filteredPosts = MediatorLiveData<List<Post>>()
    val filteredPosts: LiveData<List<Post>> = _filteredPosts

    init {
        // 1. Mediator mendengarkan perubahan pada daftar posts asli
        _filteredPosts.addSource(posts) { resource ->
            filterData(resource.data.orEmpty(), _searchQuery.value.orEmpty())
        }

        // 2. Mediator mendengarkan perubahan pada query pencarian
        _filteredPosts.addSource(_searchQuery) { query ->
            val currentPosts = posts.value?.data.orEmpty()
            filterData(currentPosts, query)
        }
    }

    private fun filterData(originalList: List<Post>, query: String) {
        // LOG 2a: Cek Query dan Ukuran Data Asli
        Log.d("FilterLog", "Pemfilteran Dimulai: Query='$query', Data Asli=${originalList.size}")

        val result = if (query.isBlank()) {
            // LOG 2b: Query Kosong
            Log.d("FilterLog", "Query kosong. Mengembalikan daftar asli.")
            originalList
        } else {
            val filteredList = originalList.filter { post ->
                // Logika pemfilteran
                post.title.contains(query, ignoreCase = true)
            }
            // LOG 2c: Hasil Filter
            Log.d("FilterLog", "Filter Selesai. Hasil ditemukan: ${filteredList.size}")
            filteredList
        }

        // Perbarui LiveData yang diamati oleh Fragment
        _filteredPosts.value = result
    }

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

    fun setSearchQuery(query: String) {
        if (_searchQuery.value != query) {
            _searchQuery.value = query
        }
    }
}