package com.example.pengelolakeuangan

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Date

private val retrofit  = Retrofit.Builder()
    .baseUrl("https://cash-flow-mate.et.r.appspot.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val MoneyService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("user") // Ganti dengan endpoint yang benar
    suspend fun getUser(): List<User>

    @GET("transaksi")
    suspend fun getTransaksi():List<Transaksi>

    @GET("daerah")
    suspend fun getDaerah():List<Daerah>

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
