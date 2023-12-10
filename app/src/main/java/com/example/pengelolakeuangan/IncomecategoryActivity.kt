package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomecategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tambahinc.setOnClickListener {
            handleTambahDialog()
        }
// Find the ImageView by ID
        val imageViewBack = findViewById<ImageView>(R.id.edit_bckinc)

        // Set an OnClickListener for the ImageView
        imageViewBack.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@IncomeCategoryActivity, activity_kategori::class.java)

            // Add any additional data to the intent if needed
            // intent.putExtra("key", "value")

            // Start the new activity
            startActivity(intent)
        }

        incomeCategoryAdapter = IncomeCategoryAdapter(kategoriList)
        binding.listinkat.apply {
            adapter = incomeCategoryAdapter
            layoutManager = LinearLayoutManager(this@IncomeCategoryActivity)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.getKategori()
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        kategoriList.addAll(response)
                        incomeCategoryAdapter.notifyDataSetChanged()
                        // Tambahkan log untuk memeriksa jumlah elemen
                        Log.d("IncomeCategoryActivity", "Jumlah kategori: ${response.size}")
                    }
                }
            } catch (e: Exception) {
                // Handle error, e.g., show a toast or log the error
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
                            // Insert the new entry into the kategori table
                            val newKategori = ApiService.Kategori(
                                id_kategori = "0", // set to an appropriate default value
                                id_jenis = "1", // id_jenis equal to 1
                                nama = newKategoriText,
                                created_at = Date(),
                                updated_at = Date()
                            )

                            // Call the API to add the new entry
                            val response = MoneyService.createKategori(newKategori)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    // If successful, update the adapter's dataset
                                    kategoriList.add(newKategori)
                                    incomeCategoryAdapter.notifyDataSetChanged()
                                } else {
                                    // Handle the error, e.g., show a toast or log the error
                                    Log.e("IncomeCategoryActivity", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            // Handle error, e.g., show a toast or log the error
                            Log.e("IncomeCategoryActivity", "Error: ${e.message}", e)
                        }
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

}
