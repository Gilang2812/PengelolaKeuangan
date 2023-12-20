package com.example.pengelolakeuangan.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.MainActivity
import com.example.pengelolakeuangan.MoneyService
import com.example.pengelolakeuangan.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PengeluaranUtil {
    suspend fun showListAset(
        context: Context, recyclerView: RecyclerView, token: String,
        onItemClickAset: (Any, Any) -> Unit
    ) {
        try {
            if (!token.isNullOrBlank()) {
                val response = MoneyService.getAset("bearer $token")
                val adapter = AsetAdapter(response, object : AsetAdapter.OnItemClickListener {
                    override fun onItemClick(nama: String, id_aset: String) {
                        onAsetItemClick(context, nama)
                        Log.d("AsetAdapter", "Item clicked: $nama, ID: $id_aset")
                    }
                })
                recyclerView.adapter = adapter
                Log.d("aset", response.toString())
                showPopupWindow(context, recyclerView, response, "aset", onItemClickAset)
            } else {
                showSnackbar(context, "Token tidak tersedia")
            }
        } catch (e: Exception) {
            Log.e("AsetDialogFragment", "Error fetching aset: ${e.message}", e)
        }
    }

    suspend fun showListKategori(
        context: Context, recyclerView: RecyclerView, token: String,
        onItemClickKategori:(Any)->Unit
    ) {
        try {
            if (!token.isNullOrBlank()) {
                val response = MoneyService.getPengeluaranKategori("bearer $token")
                val adapter = KategoriPengeluaran(response, object :KategoriPengeluaran.OnItemClickListener{
                    override fun onItemClick(nama: String) {
                        onKategoriItemClick(context,nama)
                    }
                })
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter
                showPopupWindow(context, recyclerView, response, "kategori", onItemClickKategori)
            } else {
                showSnackbar(context, "Token tidak tersedia")
            }
        } catch (e: Exception) {
            Log.e("PemasukanActivity", "Error fetching kategori: ${e.message}", e)
        }
    }

    suspend fun showListKategoriPemasukan(
        context: Context, recyclerView: RecyclerView, token: String,
        onItemClickKategori:(Any)->Unit
    ) {
        try {
            if (!token.isNullOrBlank()) {
                val response = MoneyService.getEditKategori("bearer $token")
                val adapter = KategoriPengeluaran(response, object :KategoriPengeluaran.OnItemClickListener{
                    override fun onItemClick(nama: String) {
                        onKategoriItemClick(context,nama)
                    }
                })
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter
                showPopupWindow(context, recyclerView, response, "kategori", onItemClickKategori)
            } else {
                showSnackbar(context, "Token tidak tersedia")
            }
        } catch (e: Exception) {
            Log.e("PemasukanActivity", "Error fetching kategori: ${e.message}", e)
        }
    }
    private fun showSnackbar(context: Context, message: String) {
        Snackbar.make(
            (context as View),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showPopupWindow(
        context: Context,
        recyclerView: RecyclerView,
        data: List<Any>,
        dataType: String,
        itemClickListener: Any
    ) {
        val popupWindow = PopupWindow(context)
        val listView = View.inflate(context, R.layout.fragment_asset_items, null)
        popupWindow.contentView = listView

        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val popupHeight = (screenHeight * 2 / 5)
        popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow.height = popupHeight

        popupWindow.isFocusable = true

        val adapter: RecyclerView.Adapter<*> = when (dataType) {
            "kategori" -> KategoriPengeluaran(data as List<KatPengeluaran>, object : KategoriPengeluaran.OnItemClickListener {
                override fun onItemClick(nama: String) {
                    onKategoriItemClick(context, nama)
                }
            })
            "aset" -> {
                AsetAdapter(data as List<Aset>, object : AsetAdapter.OnItemClickListener {
                    override fun onItemClick(nama: String, id_aset: String) {
                        onAsetItemClick(context, nama)
                        Log.d("AsetAdapter", "Item clicked: $nama, ID: $id_aset")
                    }
                })
            }
            else -> throw IllegalArgumentException("Invalid data type: $dataType")
        }

        recyclerView.adapter = adapter

        listView.findViewById<RecyclerView>(R.id.list_asset).adapter = adapter

        popupWindow.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0)
    }


    private fun onAsetItemClick(context: Context, nama: String) {
        if (context is Activity) {
            val inputAset = context.findViewById<AutoCompleteTextView>(R.id.input_aset_peng)
            inputAset.setText(nama)
            Log.d("PengeluaranActivity", "Item clicked2: $nama")
        }
    }

    private fun onKategoriItemClick(context: Context, nama: Any) {
        if (context is Activity) {
            val inputKategori = context.findViewById<AutoCompleteTextView>(R.id.input_kategori_peng)
            inputKategori.setText(nama.toString())
            Log.d("PengeluaranActivity", "Kategori clicked: $nama")
        }
    }

    fun updateTransaksi(
        context: Context,
        token: String,
        transaksiId:String,
        tanggal:String,
        total: Int,
        kategori:String, aset:String, note:String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = MoneyService.updateTransaksi(
                    transaksiId,
                    UpdateTransaksiRequest(
                        id_kategori = kategori,
                        id_aset = aset,
                        tanggal = tanggal,
                        jumlah = total,
                        note = note
                    ),
                    "Bearer $token"
                ).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val updatedTransaksi = response.body()

                        // Handle the updated transaction
                        Log.d("UpdateTransaksi", "Transaksi updated successfully: $updatedTransaksi")

                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        Log.e(
                            "UpdateTransaksi",
                            "Gagal: ${response.code()}, ${response.errorBody()?.string()}"
                        )
                        Toast.makeText(
                            context,
                            "Gagal mengupdate transaksi. Silakan coba lagi.  ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("UpdateTransaksi", "Exception: ${e.message}")

                Toast.makeText(
                    context,
                    "Terjadi kesalahan. Silakan coba lagi. ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
