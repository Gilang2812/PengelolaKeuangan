package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pengelolakeuangan.databinding.ActivityIncomecategoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncomeCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomecategoryBinding
    private lateinit var incomeCategoryAdapter: IncomeCategoryAdapter
    private val kategoriList: MutableList<ApiService.Kategori> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomecategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        incomeCategoryAdapter = IncomeCategoryAdapter(kategoriList)
        binding.listinkat.adapter = incomeCategoryAdapter
        binding.listinkat.layoutManager = LinearLayoutManager(this)

        // Tambahkan kode berikut di dalam onCreate
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
}
