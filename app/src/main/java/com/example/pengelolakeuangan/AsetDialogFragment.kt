package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.AsetAdapter
import kotlinx.coroutines.launch
class AsetDialogFragment : DialogFragment() {

    private  lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_asset_items, container, false)

        recyclerView = view.findViewById(R.id.list_asset)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        lifecycleScope.launch {
            try {

                val response = MoneyService.getAset("bearer ${token}")
                val adapter = AsetAdapter(response)
                recyclerView.adapter =adapter

            }catch (e:Exception){

            }
        }


        return view
    }
}

