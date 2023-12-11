package com.example.pengelolakeuangan.adapter

import com.google.gson.annotations.SerializedName

data class User(
    val id_user: String,
    val id_daerah: String,
    val nama: String,
    val email: String,
    val password: String,
    val created_at: String,
    val updated_at: String
)

data class SignUpResponse(
    @SerializedName("success") val success: String,
    @SerializedName("data") val user: User
)
