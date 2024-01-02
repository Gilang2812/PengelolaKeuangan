package com.example.pengelolakeuangan

// LocationActivity.kt
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var latitudeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var longitudeAutoCompleteTextView: AutoCompleteTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Inisialisasi variabel
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        latitudeTextView = findViewById(R.id.latitude)
        longitudeTextView = findViewById(R.id.longitude)
        latitudeAutoCompleteTextView = findViewById(R.id.textlatitude)
        longitudeAutoCompleteTextView = findViewById(R.id.textlongitude)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Izin sudah diberikan, lanjutkan dengan mendapatkan lokasi
            getLastLocation()
        } else {
            // Meminta izin lokasi
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handling permisson not granted
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Gunakan data lokasi di sini


                    // Set teks pada AutoCompleteTextView
                    latitudeAutoCompleteTextView.setText(latitude.toString())
                    longitudeAutoCompleteTextView.setText(longitude.toString())
                }
            }
    }
    fun onBackPressed(view: View) {
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // Jika izin diberikan, dapatkan lokasi
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    // Izin ditolak, Anda mungkin ingin memberikan pesan kepada pengguna
                }
                return
            }

        }
    }
}
