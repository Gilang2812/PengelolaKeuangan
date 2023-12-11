package com.example.pengelolakeuangan

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PemasukanFragment : Fragment() {

    private val calendar = Calendar.getInstance()

    private val kategoriItems = arrayOf("Kategori 1", "Kategori 2", "Kategori 3", "Lainnya")
    private val asetItems = arrayOf("Aset 1", "Aset 2", "Aset 3", "Lainnya")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pemasukan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val currentDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val textInputLayout: TextInputLayout = view.findViewById(R.id.tanggalTextView)
            val tanggalEditText: EditText = textInputLayout.findViewById(R.id.input_pemasukan)

            tanggalEditText.setText("$currentDate")

        } catch (e: Exception) {
            Log.e("Pemasukan Fragment", "Terjadi kesalahan: ${e.message}", e)
        }

        view.findViewById<View>(R.id.input_pemasukan).setOnClickListener {
            showDateTimePicker(view)
        }

        view.findViewById<View>(R.id.input_kategori).setOnClickListener {
            showListKategori(view)
        }

        view.findViewById<View>(R.id.input_aset).setOnClickListener {
            showListAset(view)
        }
    }

    private fun showDateTimePicker(view: View) {
        val inputPemasukan: EditText = view.findViewById(R.id.input_pemasukan)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    requireContext(),
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

    private fun showListKategori(view: View) {
        val inputKategori: AutoCompleteTextView = view.findViewById(R.id.input_kategori)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, kategoriItems)
        inputKategori.setAdapter(adapter)

        inputKategori.setOnItemClickListener { _, _, position, _ ->
            val selectedKategori = adapter.getItem(position)
            inputKategori.setText(selectedKategori)
        }

        inputKategori.showDropDown()
    }

    private fun showListAset(view: View) {
        val inputAset: TextInputEditText = view.findViewById(R.id.input_aset)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Aset")
            .setItems(asetItems) { _: DialogInterface?, which: Int ->
                val selectedAset = asetItems[which]
                inputAset.setText(selectedAset)
            }

        val dialog = builder.create()
        dialog.show()
    }
}
