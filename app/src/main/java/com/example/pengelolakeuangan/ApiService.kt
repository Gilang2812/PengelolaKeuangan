package com.example.pengelolakeuangan

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit  = Retrofit.Builder()
    .baseUrl("https://cash-flow-mate.et.r.appspot.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val MoneyService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("user") // Ganti dengan endpoint yang benar
    suspend fun getUser(): List<User>

    data class User(
        val id_user: String,
        val id_daerah: String,
        val nama: String,
        val email: String,
        val password: String,
        val created_at: String,
        val updated_at: String
    )

    data class UserResponse(val user  : List<User>)
}
