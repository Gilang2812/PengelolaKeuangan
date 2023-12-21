package com.example.pengelolakeuangan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.PengeluaranUtil
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditPemasukanActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_edit_pemasukan)
        val recycler = layoutInflater.inflate(R.layout.fragment_asset_items, null)
        recyclerView = recycler.findViewById(R.id.list_asset)
        recyclerView.layoutManager = LinearLayoutManager(this)


        token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()
        val intent = intent
        val transaksiId = intent.getStringExtra("transaksiId")
        val tanggalString = intent.getStringExtra("tanggal")
        val kategori = intent.getStringExtra("kategori") ?: "Kategori Tidak Diketahui"
        val aset = intent.getStringExtra("aset")
        val total = intent.getIntExtra("total", 0)
        val note = intent.getStringExtra("note")

        // Parsing tanggal kembali ke objek Date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val tanggal = dateFormat.parse(tanggalString)
        val dateFormatOutput = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val tanggalFormatted = dateFormatOutput.format(tanggal)
        tanggalInput = findViewById(R.id.edit_tanggal)
        totalInput = findViewById(R.id.edit_total)
        kategoriInput = findViewById(R.id.input_kategori_peng)
        asetInput = findViewById(R.id.input_aset_peng)
        catatanInput = findViewById(R.id.edit_note)

        tanggalInput.setText(tanggalFormatted.toString())
        totalInput.setText(total.toString())
        kategoriInput.setText(kategori)
        asetInput.setText(aset)
        catatanInput.setText(note)


        tanggalInput.setOnClickListener{
            showDateTimePickerPeng()
        }

        asetInput.setOnClickListener {
            lifecycleScope.launch {
                PengeluaranUtil.showListAset(this@EditPemasukanActivity, recyclerView, token) { nama, idAset ->
                    onAsetItemClick(this@EditPemasukanActivity, nama)
                }
            }
        }
        kategoriInput.setOnClickListener {
            lifecycleScope.launch {
                PengeluaranUtil.showListKategoriPemasukan(this@EditPemasukanActivity, recyclerView, token,){ nama ->
                    onKategoriItemClick(this@EditPemasukanActivity, nama)
                }
            }
        }
        findViewById<Button>(R.id.simpanTransaksi).setOnClickListener {
            val edittanggal = findViewById<TextInputEditText>(R.id.edit_tanggal).text.toString()
            val edittotal = findViewById<AutoCompleteTextView>(R.id.edit_total).text.toString().toIntOrNull()
            val editkategori = findViewById<AutoCompleteTextView>(R.id.input_kategori_peng).text.toString()
            val editaset = findViewById<AutoCompleteTextView>(R.id.input_aset_peng).text.toString()
            val editnote = findViewById<AutoCompleteTextView>(R.id.edit_note).text.toString()

            if (edittotal != null) {
                PengeluaranUtil.updateTransaksi(
                    this@EditPemasukanActivity,
                    token,
                    transaksiId.toString(),
                    edittanggal,
                    edittotal,
                    editkategori,
                    editaset,
                    editnote
                )
            }
        }
        findViewById<Button>(R.id.hapusTransaksi).setOnClickListener {
            PengeluaranUtil.deleteTransaksi(this@EditPemasukanActivity,token,transaksiId.toString())
        }
    }
    fun onBackPressed(view: View) {
        val intent = Intent(this@EditPemasukanActivity, MainActivity::class.java)
        startActivity(intent)
    }
    fun showDateTimePickerPeng() {
        val inputPemasukan: EditText = findViewById(R.id.edit_tanggal)

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

    private fun onAsetItemClick(context: Context, nama: Any) {
        val inputAset = (context as AddPengeluaranActivity).findViewById<AutoCompleteTextView>(R.id.input_aset_peng)
        inputAset.setText(nama.toString())
        Log.d("PengeluaranActivity", "Item clicked2: $nama")
    }

    private fun onKategoriItemClick(context: Context, nama: Any) {
        val inputKategori = (context as AddPengeluaranActivity).findViewById<AutoCompleteTextView>(R.id.input_kategori_peng)
        inputKategori.setText(nama.toString())
        Log.d("PengeluaranActivity", "Kategori clicked: $nama")
    }
}