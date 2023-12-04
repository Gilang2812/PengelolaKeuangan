// Anggaran1Activity.kt

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pengelolakeuangan.R

class Anggaran1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anggaran1)

        // Find buttons by ID
        val btnTransaction = findViewById<Button>(R.id.btnTransaction)
        val btnBudget = findViewById<Button>(R.id.btnBudget)
        val btnMore = findViewById<Button>(R.id.btnMore)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        // Set onClickListeners for each button (you can implement your logic here)
        btnTransaction.setOnClickListener {
            // Handle transaction button click
        }

        btnBudget.setOnClickListener {
            // Handle budget button click
        }

        btnMore.setOnClickListener {
            // Handle more button click
        }

        btnProfile.setOnClickListener {
            // Handle profile button click
        }
    }
}
