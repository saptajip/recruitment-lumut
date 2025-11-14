package com.example.saptajiprasetyo.data.repository

import com.example.saptajiprasetyo.data.network.RetrofitClient

class PostRepository {
    suspend fun getPost() = RetrofitClient.api.getPost()
}