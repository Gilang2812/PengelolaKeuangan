package com.example.pengelolakeuangan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.AsetAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PemasukanActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputAset: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemasukan)

        val recycler = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        recyclerView = recycler.findViewById(R.id.list_asset)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val inflatedView = layoutInflater.inflate(R.layout.content_pemasukan, null)
        inputAset = inflatedView.findViewById(R.id.input_aset)

        try {
            val currentDate =
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val textInputLayout: TextInputLayout = findViewById(R.id.tanggalTextView)
            val tanggalEditText: EditText = textInputLayout.findViewById(R.id.input_pemasukan)

            tanggalEditText.setText("$currentDate")

        } catch (e: Exception) {
            Log.e("Pemasukan Activity", "Terjadi kesalahan: ${e.message}", e)
        }

        findViewById<View>(R.id.input_pemasukan).setOnClickListener {
            showDateTimePicker()
        }

        findViewById<View>(R.id.input_kategori).setOnClickListener {
            showListKategori(it)
        }

        findViewById<View>(R.id.input_aset).setOnClickListener {
            showListAset()
        }
    }

    private fun showDateTimePicker() {
        val inputPemasukan: EditText = findViewById(R.id.input_pemasukan)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        val dateFormat =
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        inputPemasukan.setText(dateFormat.format(calendar.time))
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showListKategori(anchorView: View) {
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this)
        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getAset("bearer $token")
                    val adapter = AsetAdapter(response, this@PemasukanActivity)
                    recyclerView.adapter = adapter
                    Log.d("aset", response.toString())
                } else {
                    Snackbar.make(
                        anchorView,
                        "Token tidak tersedia",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("AsetDialogFragment", "Error fetching aset: ${e.message}", e)
            }
        }

        val popupWindow = PopupWindow(this)
        val listView = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        popupWindow.contentView = listView
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true

        // Setup adapter RecyclerView untuk listView
        val adapter = AsetAdapter(emptyList(), this@PemasukanActivity)
        listView.findViewById<RecyclerView>(R.id.list_asset).adapter = adapter

        // Tetapkan item click listener jika diperlukan
        adapter.setOnItemClickListener(object : AsetAdapter.OnItemClickListener {
            override fun onItemClick(nama: String, idAset: String) {
                // Tangani klik item di sini
                // Sebagai contoh, Anda dapat menyembunyikan popup dan memperbarui TextInputEditText Anda
                popupWindow.dismiss()
                inputAset.setText(idAset)
            }
        })

        // Tampilkan popup window di bawah anchor view
        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.BOTTOM)
    }


    private fun showListAset() {
        val dialog = AsetDialogFragment()
        dialog.show(supportFragmentManager, "AsetDialogFragment")
    }
    fun onItemClick(nama: String, id_aset: String) {


        inputAset?.setText(id_aset)
        Log.d("AsetDialogFragment", "Item clicked at position: ${inputAset?.text}")


    }
}
