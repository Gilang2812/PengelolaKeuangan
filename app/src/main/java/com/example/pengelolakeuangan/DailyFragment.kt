package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_list, container, false)
        recyclerView = view.findViewById(R.id.list)

        lifecycleScope.launch {
            try {
                val response = MoneyService.getTransaksi()
                val transaksiList = response
                val adapter = TransaksiAdapter(transaksiList)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("DailyFragment", "Error fetching transaksis: ${e.message}", e)
                Snackbar.make(requireView(), "Error di daily fragment: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { showPopupMenu() }

        return view
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), fab)
        popupMenu.menuInflater.inflate(R.menu.menu_fab, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_pemasukan -> {
                    Snackbar.make(requireView(), "pemasukan clicked", Snackbar.LENGTH_SHORT).show()
                    lifecycleScope.launch(Dispatchers.Main) {
                        try {

                        } catch (e: Exception) {
                            Snackbar.make(requireView(), "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                R.id.menu_pengeluaran -> {
                    Snackbar.make(requireView(), "Pengeluaran clicked", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}

class TransaksiAdapter(private val transaksiList: List<ApiService.Transaksi>) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        holder.bind(transaksi)
    }

    override fun getItemCount(): Int {
        return transaksiList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaksi: ApiService.Transaksi) {
            itemView.findViewById<TextView>(R.id.listpeng).text = transaksi.jumlah.toString()
        }
    }
}
