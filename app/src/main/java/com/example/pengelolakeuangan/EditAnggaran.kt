package com.example.pengelolakeuangan

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

class EditAnggaran : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var nilai: EditText
    private lateinit var nama: EditText
    private lateinit var idAnggaran: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_anggaran)
        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        nilai = findViewById(R.id.editNominal)
        nama = findViewById(R.id.editNama)

        idAnggaran = intent.getStringExtra("idAnggaran") ?: ""
        val namaEdit = intent.getStringExtra("nama") ?: ""
        val nominalEdit = intent.getStringExtra("nilai") ?: ""

        nama.setText(namaEdit)
        nilai.setText(nominalEdit)
    }

    fun onBackPressed(view: View) {
        finish()
    }

    fun updateAnggaran(view: View) {
        val namaAnggaran = nama.text.toString()
        val nilaiAnggaran = nilai.text.toString().toInt()

        val anggaranRequest = AnggaranRequest(namaAnggaran, nilaiAnggaran)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.updateAnggaran(idAnggaran, "Bearer $token", anggaranRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Pembaruan berhasil, lakukan tindakan sesuai kebutuhan
                        Log.d("EditAnggaran", "Anggaran berhasil diperbarui")
                        finish()
                    } else {
                        Log.e("EditAnggaran", "Gagal: ${response.code()}, ${response.errorBody()?.string()}")
                        // Handle kesalahan jika diperlukan
                    }
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                Log.e("EditAnggaran", "HttpException: ${e.message}")
                // Handle kesalahan jika diperlukan
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("EditAnggaran", "Exception: ${e.message}")
                // Handle exception jika diperlukan
            }
        }
    }
}
