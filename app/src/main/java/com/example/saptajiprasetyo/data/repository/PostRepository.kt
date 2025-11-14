package com.example.saptajiprasetyo.data.repository

import com.example.saptajiprasetyo.data.model.Post
import com.example.saptajiprasetyo.data.network.RetrofitClient
import retrofit2.Response

class PostRepository {
    private val apiService = RetrofitClient.api
    suspend fun getPost() = apiService.getPost()

    suspend fun getPostDetail(id: Int): Response<Post> {
        return apiService.getPostDetail(id)
    }
}