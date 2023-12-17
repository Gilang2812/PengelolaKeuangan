package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pengelolakeuangan.adapter.IncomeCategoryAdapter
import com.example.pengelolakeuangan.databinding.ActivityIncomecategoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class IncomeCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomecategoryBinding
    private lateinit var incomeCategoryAdapter: IncomeCategoryAdapter
    private val kategoriList: MutableList<ApiService.Kategori> = mutableListOf()
    private lateinit var token: String // Declare the property without initialization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomecategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the token here
        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        binding.tambahinc.setOnClickListener {
            handleTambahDialog()
        }

        val imageViewBack = findViewById<ImageView>(R.id.edit_bckinc)

        imageViewBack.setOnClickListener {
            val intent = Intent(this@IncomeCategoryActivity, activity_kategori::class.java)
            startActivity(intent)
        }

        incomeCategoryAdapter = IncomeCategoryAdapter(kategoriList)
        binding.listinkat.apply {
            adapter = incomeCategoryAdapter
            layoutManager = LinearLayoutManager(this@IncomeCategoryActivity)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.getKategori("Bearer $token")
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        kategoriList.clear()
                        kategoriList.addAll(response)
                        incomeCategoryAdapter.updateDataset(kategoriList)
                        Log.d("IncomeCategoryActivity", "Jumlah kategori: ${response.size}")
                    }
                }
            } catch (e: Exception) {
                Log.e("IncomeCategoryActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun handleTambahDialog() {
        val view = layoutInflater.inflate(R.layout.activity_tambahincome, null)
        val kategoriEdit = view.findViewById<EditText>(R.id.kategoriEdit)

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Tambah") { _, _ ->
                val newKategoriText = kategoriEdit.text.toString().trim()

                if (newKategoriText.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val newKategori = ApiService.Kategori(
                                id_kategori = "", // Let the server generate the ID
                                id_jenis = "1",
                                nama = newKategoriText,
                                created_at = Date(),
                                updated_at = Date()
                            )

                            val response = MoneyService.createKategori(newKategori, "Bearer $token")

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    kategoriList.add(newKategori)
                                    incomeCategoryAdapter.notifyDataSetChanged()
                                } else {
                                    Log.e("IncomeCategoryActivity", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("IncomeCategoryActivity", "Error: ${e.message}", e)
                        }
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
