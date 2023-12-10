package com.example.pengelolakeuangan

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Date

private val retrofit  = Retrofit.Builder()
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
    ): Response<User>


    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    data class LoginRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String
    )


    data class LoginResponse(
        @SerializedName("token") val token: String,
        @SerializedName("message") val message: String,
        @SerializedName("user") val user: User
    )

    @GET("user") // Ganti dengan endpoint yang benar
    suspend fun getUser(): List<User>

    @GET("transaksi")
    suspend fun getTransaksi(@Header("Authorization") authorization: String): List<Transaksi>

    @GET("daerah")
    suspend fun getDaerah():List<Daerah>

    data class User(
        val id_user: String,
        val id_daerah: String,
        val nama: String,
        val email: String,
        val password: String,
        val created_at: String,
        val updated_at: String
    )
    data class Daerah (
        val id_daerah: String,
        val nama_daerah: String,
        val umr :Int,
        val created_at: String,
        val updated_at: String
    )
    data class Transaksi(
        val id_transaksi: String,
        val id_user: String,
        val id_kategori: String,
        val id_jenis: String,
        val id_aset: String,
        val tanggal: Date,
        val jumlah: Int,
        val note: String,
        val created_at: Date,
        val updated_at: Date
    )



}
