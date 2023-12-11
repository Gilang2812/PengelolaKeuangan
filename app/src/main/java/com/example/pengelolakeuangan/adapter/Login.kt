package com.example.pengelolakeuangan.adapter

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: User
)
