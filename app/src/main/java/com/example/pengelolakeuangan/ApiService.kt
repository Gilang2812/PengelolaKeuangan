package com.example.pengelolakeuangan

import com.example.pengelolakeuangan.adapter.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .baseUrl("https://ptbfahriganteng.et.r.appspot.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val MoneyService = retrofit.create(ApiService::class.java)

interface ApiService {
    @FormUrlEncoded
    @POST("user")
    suspend fun createUser(
        @Field("id_daerah") id_daerah: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SignUpResponse>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("user")
    suspend fun getUser(): List<User>

    @GET("transaksi")
    suspend fun getTransaksi(@Header("Authorization") authorization: String): List<Transaksi>

    @GET("daerah")
    suspend fun getDaerah(): List<Daerah>
}
