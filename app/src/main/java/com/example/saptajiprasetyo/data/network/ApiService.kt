package com.example.saptajiprasetyo.data.network

import com.example.saptajiprasetyo.data.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPost(): Response<List<Post>>
}