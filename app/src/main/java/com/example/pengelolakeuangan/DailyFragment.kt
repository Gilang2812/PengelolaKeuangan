package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.TransaksiAdapter
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
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    // Call getTransaksi with Authorization header
                    val response = MoneyService.getTransaksi("Bearer $token")
                    val transaksiList = response
                    val adapter =TransaksiAdapter(transaksiList)
                    recyclerView.adapter = adapter
                    Log.d("DailyFragment", response.toString())
                } else {
                    Snackbar.make(requireView(), "Token not available", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("DailyFragment", "Error fetching transaksis: ${e.message}", e)
                Snackbar.make(requireView(), "Error di daily fragment: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { showPopupMenu() }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), fab)
        popupMenu.menuInflater.inflate(R.menu.menu_fab, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_pemasukan -> {
                    Snackbar.make(requireView(), "Pemasukan clicked", Snackbar.LENGTH_SHORT).show()
                    lifecycleScope.launch(Dispatchers.Main) {
                        try {
                            // Gantikan dengan PemasukanFragment
                            val pemasukanFragment = PemasukanFragment()
                            replaceFragment(pemasukanFragment)
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
