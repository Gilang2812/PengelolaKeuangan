package com.example.pengelolakeuangan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.Aset
import com.example.pengelolakeuangan.adapter.AsetAdapter
import com.example.pengelolakeuangan.adapter.KatPengeluaran
import com.example.pengelolakeuangan.adapter.KategoriPengeluaran

import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddPengeluaranActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private lateinit var token:String
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pengeluaran)

        val recycler = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        recyclerView = recycler.findViewById(R.id.list_asset)
        recyclerView.layoutManager = LinearLayoutManager(this)

        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()
        try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            val textInputLayout: TextInputLayout = findViewById(R.id.textTanggal)
            val tanggalEditText: EditText = textInputLayout.findViewById(R.id.inputTanggalPeng)

            tanggalEditText.setText(currentDate)

        } catch (e: Exception) {
            Log.e("Pemasukan Activity", "Terjadi kesalahan: ${e.message}", e)
        }

        findViewById<View>(R.id.inputTanggalPeng).setOnClickListener {
            showDateTimePickerPeng()
        }
        findViewById<AutoCompleteTextView>(R.id.input_aset_peng).setOnClickListener{
            showListAset(it)
        }

        findViewById<AutoCompleteTextView>(R.id.input_kategori_peng).setOnClickListener {
            showListKategori(it)
        }
    }
    fun onBackPressed(view: View) {
        val intent = Intent(this@AddPengeluaranActivity, MainActivity::class.java)
        startActivity(intent)
    }
    fun showDateTimePickerPeng() {
        val inputPemasukan: EditText = findViewById(R.id.inputTanggalPeng)

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

    private fun showListAset(anchorView: View) {
        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getAset("bearer $token")
                    val adapter = AsetAdapter(response, object : AsetAdapter.OnItemClickListener {
                        override fun onItemClick(nama: String, id_aset: String) {
                            onAsetItemClick(nama)
                            Log.d("AsetAdapter", "Item clicked: $nama, ID: $id_aset")
                        }
                    })
                    recyclerView.adapter = adapter
                    Log.d("aset", response.toString())
                    showPopupWindow(anchorView, response, "aset")
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
    }

    private fun showPopupWindow(anchorView: View, data: List<Any>, dataType: String) {
        val popupWindow = PopupWindow(this)
        val listView = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        popupWindow.contentView = listView

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val popupHeight = (screenHeight * 2 / 5)
        popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow.height = popupHeight

        popupWindow.isFocusable = true

        val adapter: RecyclerView.Adapter<*> = when (dataType) {
            "kategori" -> KategoriPengeluaran( data as List<KatPengeluaran>, this@AddPengeluaranActivity)
            "aset" -> {
                AsetAdapter(data as List<Aset>, object : AsetAdapter.OnItemClickListener {
                    override fun onItemClick(nama: String, id_aset: String) {
                        onAsetItemClick(nama)
                        Log.d("AsetAdapter", "Item clicked: $nama, ID: $id_aset")
                    }
                })
            }
            else -> throw IllegalArgumentException("Invalid data type: $dataType")
        }

        listView.findViewById<RecyclerView>(R.id.list_asset).adapter = adapter

        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }
    fun onAsetItemClick(nama: String) {
        val inputAset = findViewById<AutoCompleteTextView>(R.id.input_aset_peng)
        inputAset.setText(nama)
        Log.d("PengeluaranActivity", "Item clicked2: $nama")
    }

    private fun showListKategori(anchorView: View) {
        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getPengeluaranKategori("bearer $token")
                    val adapter = KategoriPengeluaran(response, this@AddPengeluaranActivity)
                    recyclerView.layoutManager = LinearLayoutManager(this@AddPengeluaranActivity)
                    recyclerView.adapter = adapter
                    showPopupWindow(anchorView, response, "kategori")
                } else {
                    Snackbar.make(
                        anchorView,
                        "Token tidak tersedia",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("PemasukanActivity", "Error fetching kategori: ${e.message}", e)
            }
        }
    }
    fun onKategoriItemClick(nama: String) {
        val inputKategori = findViewById<AutoCompleteTextView>(R.id.input_kategori_peng)
        inputKategori.setText(nama)
        Log.d("PemasukanActivity", "Item clicked2: $nama")
    }

}
