package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AsetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_asset_items, container, false)
        view?.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams

            // Sesuaikan peekHeight sesuai kebutuhan
            params.height = (parent.height * 0.6).toInt()

            parent.layoutParams = params
        }

        // Mengaktifkan fitur klik di luar dialog
        isCancelable = true

        // Menanggapi klik di luar dialog
        dialog?.setOnShowListener {
            dialog?.findViewById<View>(com.google.android.material.R.id.touch_outside)?.setOnClickListener {
                dismiss()
            }
        }

        return view
    }

}