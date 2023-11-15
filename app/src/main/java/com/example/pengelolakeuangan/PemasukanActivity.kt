package com.example.pengelolakeuangan

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.*
import java.util.Locale

class PemasukanActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()

    private val kategoriItems = arrayOf("Kategori 1", "Kategori 2", "Kategori 3", "Lainnya")
    private val asetItems = arrayOf("Aset 1", "Aset 2", "Aset 3", "Lainnya")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemasukan)


        try {
            val currentDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val textInputLayout: TextInputLayout = findViewById(R.id.tanggalTextView)
            val tanggalEditText: EditText = textInputLayout.findViewById(R.id.input_pemasukan)

            tanggalEditText.setText("$currentDate")

        } catch (e: Exception) {
            Log.e("Pemasukan Aktivity", "Terjadi kesalahan: ${e.message}", e)
        }
    }
    fun showDateTimePicker(view: android.view.View) {
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

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
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
    fun showListKategori(view: android.view.View) {
        val items = arrayOf("Item 1", "Item 2", "Item 3") // Gantilah dengan item sesuai kebutuhan Anda

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Item")
            .setItems(items) { dialog, which ->
                val selectedItem = items[which]

                val textViewItem = findViewById<TextView>(R.id.textViewItem)
                textViewItem.text = selectedItem
            }

        val dialog = builder.create()
        dialog.show()
    }

    fun showListAset(view: android.view.View) {
        val inputAset: TextInputEditText = findViewById(R.id.input_aset)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Aset")
            .setItems(asetItems) { _: DialogInterface?, which: Int ->
                val selectedAset = asetItems[which]
                inputAset.setText(selectedAset)
            }

        val dialog = builder.create()
        dialog.show()
    }


}