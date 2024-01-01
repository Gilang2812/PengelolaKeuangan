package com.example.pengelolakeuangan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pengelolakeuangan.adapter.AnggaranRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TambahAnggaran : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var nilai: EditText
    private lateinit var nama : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_anggaran)
        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        nilai = findViewById(R.id.inputNominal)
        nama  = findViewById(R.id.inputNama)
    }

    fun onBackPressed(view: View) {
        finish()
    }
    fun addAnggaran(view: View) {
        val nama = nama.text.toString()
        val nilai = nilai.text.toString().toInt()

        val anggaraRequest = AnggaranRequest(nama,nilai)
        tambahAnggaran(anggaraRequest,token)

    }
    private fun tambahAnggaran(anggaranRequest: AnggaranRequest, authToken: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val anggaranResponse = MoneyService.tambahAnggaran("Bearer $authToken", anggaranRequest)

                withContext(Dispatchers.Main) {

                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                Log.e("TambahAnggaran", "HttpException: ${e.message}")

                // Di sini Anda dapat menangani kesalahan secara spesifik jika diperlukan
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TambahAnggaran", "Exception: ${e.message}")

                // Di sini Anda dapat menangani kesalahan secara spesifik jika diperlukan
            }
        }
    }



}