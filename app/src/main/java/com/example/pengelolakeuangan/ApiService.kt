package com.example.pengelolakeuangan

import android.widget.Toast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("transaksi")
    suspend fun getTransaksiList(
        @Header("Authorization") token: String
    ): List<Transaksi>

    @GET("user") // Ganti dengan endpoint yang benar
    suspend fun getUser(
        @Header("Authorization") token: String
    ): User

    data class Transaksi(
        val id_transaksi: String,
        val id_user: String,
        val id_kategori: String,
        val id_jenis: String,
        val id_aset: String,
        val tanggal: String,
        val jumlah: Int,
        val note: String?,
    )

    data class User(
        val id_user: String,
        val nama :String,
        val email : String,
        val password : String
    )
}
