package com.example.pengelolakeuangan

import com.example.pengelolakeuangan.adapter.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
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
    suspend fun getKategori(@Header("Authorization") authorization: String): List<Kategori>
    @POST("kategori")
    suspend fun createKategori(@Body kategori: Kategori, @Header("Authorization") authorization: String): Response<Kategori>

    @DELETE("kategori/{id_kategori}")
    suspend fun deleteKategori(@Path("id_kategori") idKategori: String, @Header("Authorization") authorization: String): Response<Unit>

    @PUT("kategori/{id_kategori}")
    suspend fun updateKategori(
        @Path("id_kategori") idKategori: String,
        @Body kategori: Kategori,
        @Header("Authorization") authorization: String
    ): Response<Kategori>

    @GET("katpemasukan")
    suspend fun getPemasukanKategori(@Header("Authorization") authorization: String) : List<KategoriPemasukan>
    @GET("katpemasukan")
    suspend fun getEditKategori(@Header("Authorization") authorization: String) : List<KatPengeluaran>
    @GET("katpengeluaran")
    suspend fun getPengeluaranKategori(@Header("Authorization") authorization: String) : List<KatPengeluaran>
    @GET("aset")
    suspend fun getAset(@Header("Authorization") authorization: String) : List<Aset>
    // Add this method to the ApiService interface
    @PUT("aset/{id_aset}")
    suspend fun updateAset(
        @Path("id_aset") idAset: String,
        @Body aset: Aset,
        @Header("Authorization") authorization: String
    ): Response<Aset>

    @POST("aset")
    suspend fun createAset(@Body aset: Aset, @Header("Authorization") authorization: String): Response<Aset>




    @POST("transaksi")
    fun postTransaksi(@Body request: TransaksiRequest, @Header("Authorization") authorization: String): Call<TransaksiResponse>

    @PUT("transaksi/{transaksiId}")
    fun updateTransaksi(
        @Path("transaksiId") transaksiId: String,
        @Body transaksiRequest: UpdateTransaksiRequest,
        @Header("Authorization") authToken: String
    ): Call<TransaksiResponse>
    data class Kategori(
        @SerializedName("id_kategori")
        val id_kategori: String,
        val id_jenis: String,
        var nama: String,
        val created_at: Date,
        val updated_at: Date
    )

    @DELETE("transaksi/{transaksiId}")
    fun deleteTransaksi(
        @Path("transaksiId") transaksiId: String,
        @Header("Authorization") authorization: String
    ): Call<DeleteTransaksiResponse>

    @GET("anggaran")
    suspend fun getAnggaran(@Header("Authorization") authorization: String): List<Anggaran>

    @POST("anggaran")
    suspend fun tambahAnggaran(
        @Header("Authorization") authorization: String,
        @Body request: AnggaranRequest
    ): AnggaranResponse

    @PUT("anggaran/{idAnggaran}")
    suspend fun updateAnggaran(
        @Path("idAnggaran") idAnggaran: String,
        @Header("Authorization") authorization: String,
        @Body request: AnggaranRequest
    ): Response<UpdateAnggaranResponse>

}
