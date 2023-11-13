package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TransactionFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.top_navigation)!!

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.daily -> {
                    if (childFragmentManager.findFragmentById(R.id.transaction_container) !is DailyFragment) {
                        replaceFragment(DailyFragment())
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.monthly -> {
                    if (childFragmentManager.findFragmentById(R.id.transaction_container) !is MonthlyFragment) {
                        replaceFragment(MonthlyFragment())
                    }
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }

        if (childFragmentManager.findFragmentById(R.id.transaction_container) == null) {
            replaceFragment(DailyFragment())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        try {
            childFragmentManager.beginTransaction().replace(R.id.transaction_container, fragment).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
