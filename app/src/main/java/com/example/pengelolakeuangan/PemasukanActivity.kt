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
import com.example.pengelolakeuangan.adapter.KategoriAdapter
import com.example.pengelolakeuangan.adapter.KategoriPemasukan
import com.example.pengelolakeuangan.adapter.TransaksiRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PemasukanActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String

    private lateinit var tanggalInput: TextInputEditText
    private lateinit var totalInput: AutoCompleteTextView
    private lateinit var kategoriInput: AutoCompleteTextView
    private lateinit var asetInput: AutoCompleteTextView
    private lateinit var catatanInput: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemasukan)

        val recycler = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        recyclerView = recycler.findViewById(R.id.list_asset)
        recyclerView.layoutManager = LinearLayoutManager(this)
        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()
        tanggalInput = findViewById(R.id.input_pemasukan)
        totalInput = findViewById(R.id.input_total)
        kategoriInput = findViewById(R.id.input_kategori)
        asetInput = findViewById(R.id.input_aset)
        catatanInput = findViewById(R.id.input_catatan)

        try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())


            val textInputLayout: TextInputLayout = findViewById(R.id.tanggalTextView)
            val tanggalEditText: EditText = textInputLayout.findViewById(R.id.input_pemasukan)

            tanggalEditText.setText("$currentDate")

        } catch (e: Exception) {
            Log.e("Pemasukan Activity", "Terjadi kesalahan: ${e.message}", e)
        }

        findViewById<View>(R.id.input_pemasukan).setOnClickListener {
            showDateTimePicker()
        }

        findViewById<AutoCompleteTextView>(R.id.input_kategori).setOnClickListener {
            showListKategori(it)
        }
        findViewById<AutoCompleteTextView>(R.id.input_aset).setOnClickListener {
            showListAset(it)
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

    private fun showListAset(anchorView: View) {
        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getAset("bearer $token")
                    val adapter = AsetAdapter(response, this@PemasukanActivity)
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
            "kategori" -> {
                KategoriAdapter(data as List<KategoriPemasukan>, this@PemasukanActivity)
            }
            "aset" -> {
                AsetAdapter(data as List<Aset>, this@PemasukanActivity)
            }
            else -> throw IllegalArgumentException("Invalid data type: $dataType")
        }

        listView.findViewById<RecyclerView>(R.id.list_asset).adapter = adapter

        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }

    fun onAsetItemClick(nama: String) {
        val inputAset = findViewById<AutoCompleteTextView>(R.id.input_aset)
        inputAset.setText(nama)
        Log.d("PemasukanActivity", "Item clicked2: $nama")
    }

    private fun showListKategori(anchorView: View) {
        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getPemasukanKategori("bearer $token")
                    val adapter = KategoriAdapter(response, this@PemasukanActivity)
                    recyclerView.layoutManager = LinearLayoutManager(this@PemasukanActivity)
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
        val inputKategori = findViewById<AutoCompleteTextView>(R.id.input_kategori)
        inputKategori.setText(nama)
        Log.d("PemasukanActivity", "Item clicked2: $nama")
    }

    fun tambahPemasukan(view: View) {
        val tanggal = tanggalInput.text.toString()
        val total = totalInput.text.toString().toInt()
        val kategori = kategoriInput.text.toString()
        val aset = asetInput.text.toString()
        val catatan = catatanInput.text.toString()

        val transaksiRequest = TransaksiRequest(
            id_kategori = kategori,
            id_jenis = "1",
            id_aset = aset,
            tanggal = tanggal,
            jumlah = total,
            note = catatan
        )

        // Lanjutkan dengan mengirimkan transaksiRequest ke server seperti sebelumnya
        postTransaksi(transaksiRequest,token)
    }
    private fun postTransaksi(transaksiRequest: TransaksiRequest, authToken:String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.postTransaksi(transaksiRequest, "Bearer $authToken").execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val transaksiResponse = response.body()

                        val intent = Intent(this@PemasukanActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("PostTransaksi", "Gagal: ${response.code()}, ${response.errorBody()?.string()}")

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PostTransaksi", "Exception: ${e.message}")
            }
        }
    }
}


