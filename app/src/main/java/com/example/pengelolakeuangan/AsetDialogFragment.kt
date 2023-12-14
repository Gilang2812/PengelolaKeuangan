package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.AsetAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AsetDialogFragment : DialogFragment(), AsetAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private var inputAset: TextInputEditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_asset_items, container, false)
        recyclerView = view.findViewById(R.id.list_asset)

        // Assuming that input_aset is in the content_pemasukan layout
        val contentPemasukanLayout = inflater.inflate(R.layout.content_pemasukan, container, true)
        inputAset = contentPemasukanLayout.findViewById(R.id.input_aset)

        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getAset("bearer $token")
//                    val adapter = AsetAdapter(response, this@AsetDialogFragment)
//                    adapter.setData(response)
//                    recyclerView.adapter = adapter
//                    Log.d("aset", response.toString())
                } else {
                    Snackbar.make(requireView(), "Token not available", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AsetDialogFragment", "Error fetching aset: ${e.message}", e)
            }
        }

        return view
    }

    override fun onItemClick(nama: String, id_aset: String) {

        inputAset?.setText(id_aset)
        Log.d("AsetDialogFragment", "Item clicked at position: ${inputAset?.text}")


    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = (resources.displayMetrics.heightPixels * 2 / 5).toInt()
            dialog.window?.setLayout(width, height)
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }
}

