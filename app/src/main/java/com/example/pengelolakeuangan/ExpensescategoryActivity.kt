package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pengelolakeuangan.adapter.ExpensesCategoryAdapter
import com.example.pengelolakeuangan.databinding.ActivityExpensescategoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class ExpensesCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpensescategoryBinding
    private lateinit var expensesCategoryAdapter: ExpensesCategoryAdapter
    private val kategoriList: MutableList<ApiService.Kategori> = mutableListOf()
    private lateinit var token: String // Declare the property without initialization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensescategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the token here
        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        binding.tambahexp.setOnClickListener {
            handleTambahDialog()
        }

        val imageViewBack = findViewById<ImageView>(R.id.edit_bckexp)

        imageViewBack.setOnClickListener {
            val intent = Intent(this@ExpensesCategoryActivity, activity_kategori::class.java)
            startActivity(intent)
        }

        expensesCategoryAdapter = ExpensesCategoryAdapter(kategoriList)
        binding.listexkat.apply {
            adapter = expensesCategoryAdapter
            layoutManager = LinearLayoutManager(this@ExpensesCategoryActivity)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.getKategori("Bearer $token")
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        kategoriList.clear()
                        kategoriList.addAll(response)
                        expensesCategoryAdapter.updateDataset(kategoriList)
                        Log.d("ExpensesCategoryActivity", "Jumlah kategori: ${response.size}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ExpensesCategoryActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun handleTambahDialog() {
        val view = layoutInflater.inflate(R.layout.activity_tambahexpenses, null)
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
                                id_jenis = "2", // Adjust based on your requirements
                                nama = newKategoriText,
                                created_at = Date(),
                                updated_at = Date()
                            )

                            val response = MoneyService.createKategori(newKategori, "Bearer $token")

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    kategoriList.add(newKategori)
                                    expensesCategoryAdapter.notifyDataSetChanged()
                                } else {
                                    Log.e("ExpensesCategoryActivity", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ExpensesCategoryActivity", "Error: ${e.message}", e)
                        }
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
