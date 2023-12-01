package com.example.pengelolakeuangan

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit  = Retrofit.Builder()
    .baseUrl("http:localhost:3000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val MoneyService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("user") // Ganti dengan endpoint yang benar
    suspend fun getUser(): UserResponse

    data class User(
        val id_user:String,
        val nama :String,
        val email : String,
        val password : String
    )

    data class UserResponse(val user  : List<User>)
}
