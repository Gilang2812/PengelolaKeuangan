import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Anggaran1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anggaran1)

        val editTextBiaya = findViewById<EditText>(R.id.editTextBiaya)
        val buttonHitung = findViewById<Button>(R.id.buttonHitung)
        val textViewHasil = findViewById<TextView>(R.id.textViewHasil)

        buttonHitung.setOnClickListener {
            // Mendapatkan nilai biaya dari EditText
            val biayaStr = editTextBiaya.text.toString()

            if (biayaStr.isNotEmpty()) {
                // Menghitung anggaran, misalnya dengan mengalikan biaya dengan faktor tertentu
                val biaya = biayaStr.toDouble()
                val faktor = 1.2 // Anda dapat mengganti faktor sesuai kebutuhan
                val anggaran = biaya * faktor

                // Menampilkan hasil di TextView
                textViewHasil.text = "Anggaran: $$anggaran"
            } else {
                // Menampilkan pesan jika EditText kosong
                textViewHasil.text = "Masukkan biaya terlebih dahulu."
            }
        }
    }
}
