package com.example.saptajiprasetyo.data.network

import com.example.saptajiprasetyo.data.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("todos")
    suspend fun getPost(): Response<List<Post>>

    @GET("todos/{id}")
    suspend fun getPostDetail(@Path("id") id: Int): Response<Post>
}