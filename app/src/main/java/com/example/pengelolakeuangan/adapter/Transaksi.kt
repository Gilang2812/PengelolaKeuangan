package com.example.pengelolakeuangan.adapter

import java.util.Date

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
