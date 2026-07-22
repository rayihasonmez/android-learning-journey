package com.rayiha.weather_app

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rayiha.weather_app.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import com.rayiha.weather_app.api.RetrofitClient
import com.rayiha.weather_app.utils.Constants
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
class MainActivity : AppCompatActivity() {





    private companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Konum izni reddedildi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    fun String.ilkHarfiBuyukYap(): String {
        if (this.isEmpty()) return this
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getCurrentLocation()

        } else {

            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        }

        setContentView(view)



        binding.btnSearch.setOnClickListener {
            val city = binding.editTextText.text.toString()


                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.api.getWeather(
                            city = city,
                            apiKey = Constants.API_KEY
                        )

                        val iconCode = response.weather[0].icon
                        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

                        binding.tvTemperature.text = "${response.main.temp}°C"
                        binding.tvHumidity.text = "Humidity: ${response.main.humidity}%"
                        binding.tvWindSpeed.text = "Wind: ${response.wind.speed} m/s"
                        binding.tvDescription.text =
                            "${response.weather[0].description.ilkHarfiBuyukYap()}"
                        binding.tvName.text = "${response.name}"
                        binding.tvFeelsLike.text = "Feels Like: ${response.main.feels_like}°C"
                        binding.tvPressure.text = "Pressure: ${response.main.pressure}hPa"
                        binding.ivWeatherIcon.load(iconUrl)


                    }
                    catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,                   // Context (burada MainActivity)
                            "Şehir bulunamadı",     // Gösterilecek mesaj
                            Toast.LENGTH_SHORT      // Süre
                        ).show()
                    }
            }}

        }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getCurrentLocation() {

        Log.d("LOCATION", "Fonksiyon çalıştı")

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    Log.d("LOCATION", "Lat: $latitude")
                    Log.d("LOCATION", "Lon: $longitude")
                    lifecycleScope.launch {

                        try {

                            val response = RetrofitClient.api.getWeatherByLocation(
                                lat = latitude,
                                lon = longitude,
                                apiKey = Constants.API_KEY
                            )

                        } catch (e: Exception) {

                            Log.e("WEATHER", "Hata: ${e.message}")

                        }

                    }

                } else {

                    Log.d("LOCATION", "Location NULL")

                }

            }
            .addOnFailureListener { e ->

                Log.e("LOCATION", "Hata: ${e.message}")

            }
    }
}
