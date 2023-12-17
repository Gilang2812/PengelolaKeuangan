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
import java.util.Date
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

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
        val nama: String,
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

    data class Kategori(
        @SerializedName("id_kategori")
        val id_kategori: String,
        val id_jenis: String,
        val nama: String,
        val created_at: Date,
        val updated_at: Date
    )


}
