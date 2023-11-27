package com.example.pengelolakeuangan

import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("transaksi")
    suspend fun getTransaksiList(
        @Header("Authorization") token: String
    ): List<Transaksi>

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
}
