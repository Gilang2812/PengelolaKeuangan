package com.example.pengelolakeuangan

import com.example.pengelolakeuangan.adapter.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.Date

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

    @GET("kategori") // Ganti "kategori" dengan endpoint yang sesuai
    suspend fun getKategori(): List<Kategori>
    @POST("kategori")
    suspend fun createKategori(@Body kategori: Kategori): Response<Kategori>

    @DELETE("kategori/{id_kategori}")
    suspend fun deleteKategori(@Path("id_kategori") idKategori: String): Response<Unit>


    @GET("aset")
    suspend fun getAset(@Header("Authorization") authorization: String) : List<Aset>
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    data class Kategori(
        @SerializedName("id_kategori")
        val id_kategori: String,
        val id_jenis: String,
        val nama: String,
        val created_at: Date,
        val updated_at: Date
    )


}
