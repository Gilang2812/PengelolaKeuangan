package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.pengelolakeuangan.placeholder.PlaceholderContent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class DailyFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

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
                   val intent = Intent(requireContext(), PemasukanActivity::class.java)
                    startActivity(intent)
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
