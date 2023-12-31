    package com.example.pengelolakeuangan

    import android.app.DatePickerDialog
    import android.app.TimePickerDialog
    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.AutoCompleteTextView
    import android.widget.EditText
    import android.widget.TimePicker
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.pengelolakeuangan.adapter.PengeluaranUtil
    import com.example.pengelolakeuangan.adapter.TransaksiRequest
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

    class AddPengeluaranActivity : AppCompatActivity() {
        private val calendar = Calendar.getInstance()
        private lateinit var token: String
        private lateinit var recyclerView: RecyclerView

        private lateinit var tanggalInput: TextInputEditText
        private lateinit var totalInput: AutoCompleteTextView
        private lateinit var kategoriInput: AutoCompleteTextView
        private lateinit var asetInput: AutoCompleteTextView
        private lateinit var catatanInput: AutoCompleteTextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_pengeluaran)

            val recycler = layoutInflater.inflate(R.layout.fragment_asset_items, null)
            recyclerView = recycler.findViewById(R.id.list_asset)
            recyclerView.layoutManager = LinearLayoutManager(this)

            tanggalInput = findViewById(R.id.inputTanggalPeng)
            totalInput = findViewById(R.id.input_totalPeng)
            kategoriInput = findViewById(R.id.input_kategori_peng)
            asetInput = findViewById(R.id.input_aset_peng)
            catatanInput = findViewById(R.id.input_catatan_peng)

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

            findViewById<AutoCompleteTextView>(R.id.input_aset_peng).setOnClickListener {
                lifecycleScope.launch {
                    PengeluaranUtil.showListAset(this@AddPengeluaranActivity, recyclerView, token) { nama, idAset ->
                        onAsetItemClick(this@AddPengeluaranActivity, nama)
                    }
                }
            }

            findViewById<AutoCompleteTextView>(R.id.input_kategori_peng).setOnClickListener {
                lifecycleScope.launch {
                    PengeluaranUtil.showListKategori(this@AddPengeluaranActivity, recyclerView, token,){ nama ->
                        onKategoriItemClick(this@AddPengeluaranActivity, nama)
                    }
                }
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

        fun tambahPengeluaran(view: View) {
            val tanggal = tanggalInput.text.toString()
            val total = totalInput.text.toString().toInt()
            val kategori = kategoriInput.text.toString()
            val aset = asetInput.text.toString()
            val catatan = catatanInput.text.toString()

            val transaksiRequest = TransaksiRequest(
                id_kategori = kategori,
                id_jenis = "2",
                id_aset = aset,
                tanggal = tanggal,
                jumlah = total,
                note = catatan
            )

            postTransaksi(transaksiRequest, token)
        }

        private fun postTransaksi(transaksiRequest: TransaksiRequest, authToken: String) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response =
                        MoneyService.postTransaksi(transaksiRequest, "Bearer $authToken").execute()

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val transaksiResponse = response.body()

                            val intent = Intent(this@AddPengeluaranActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.e(
                                "PostTransaksi",
                                "Gagal: ${response.code()}, ${response.errorBody()?.string()}"
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("PostTransaksi", "Exception: ${e.message}")
                }
            }
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
