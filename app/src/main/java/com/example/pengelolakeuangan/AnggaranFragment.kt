package  com.example.pengelolakeuangan
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.AnggaranAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AnggaranFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView


    // Define launcher
    private val addAnggaranLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Lakukan pembaruan tampilan di sini
            lifecycleScope.launch {
                try {
                    val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())
                    if (!token.isNullOrBlank()) {
                        val response = MoneyService.getAnggaran("Bearer $token")
                        val adapter = AnggaranAdapter(response)
                        adapter.setData(response)
                        recyclerView.adapter = adapter
                        Log.d("Anggaran Fragment", response.toString())
                    } else {
                        Snackbar.make(requireView(), "Token not available", Snackbar.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("AnggaranFragment", "Error fetching anggaran: ${e.message}", e)
                    Snackbar.make(requireView(), "Error di anggaran fragment: ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anggaran, container, false)

        recyclerView = view.findViewById(R.id.list)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    val response = MoneyService.getAnggaran("Bearer $token")
                    val adapter = AnggaranAdapter(response)
                    adapter.setData(response)
                    recyclerView.adapter = adapter
                    Log.d("Anggaran Fragment", response.toString())
                } else {
                    Snackbar.make(requireView(), "Token not available", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AnggaranFragment", "Error fetching anggaran: ${e.message}", e)
                Snackbar.make(requireView(), "Error di anggaran fragment: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        fab = view.findViewById(R.id.tambahAnggaran)
        fab.setOnClickListener {
            // Menggunakan launcher untuk memulai aktivitas tambah anggaran
            addAnggaranLauncher.launch(Intent(requireContext(), TambahAnggaran::class.java))
        }

        return view
    }
}
