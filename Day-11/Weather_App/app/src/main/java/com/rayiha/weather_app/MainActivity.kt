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
import com.rayiha.weather_app.model.WeatherResponse
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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
//            binding.weatherUI.visibility = View.GONE
//            binding.progressBar.visibility = View.VISIBLE
            getCurrentLocation()

        } else {

            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        }

        setContentView(view)


        binding.tvCity.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Search City")
                .setMessage("Dialog başarıyla açıldı!")
                .setPositiveButton("OK", null)
                .show()
        }
//        binding.btnSearch.setOnClickListener {
//            val city = binding.editTextText.text.toString()
//
//
//                lifecycleScope.launch {
//                    try {
//                        val response = RetrofitClient.api.getWeather(
//                            city = city,
//                            apiKey = Constants.API_KEY
//                        )
//                        updateWeatherUI(response)
//
////                        val iconCode = response.weather[0].icon
////                        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
////
////                        binding.tvTemperature.text = "${response.main.temp}°C"
////                        binding.tvHumidity.text = "Humidity: ${response.main.humidity}%"
////                        binding.tvWindSpeed.text = "Wind: ${response.wind.speed} m/s"
////                        binding.tvDescription.text =
////                            "${response.weather[0].description.ilkHarfiBuyukYap()}"
////                        binding.tvName.text = "${response.name}"
////                        binding.tvFeelsLike.text = "Feels Like: ${response.main.feels_like}°C"
////                        binding.tvPressure.text = "Pressure: ${response.main.pressure}hPa"
////                        binding.ivWeatherIcon.load(iconUrl)
//
//
//                    }
//                    catch (e: Exception) {
//                        Toast.makeText(
//                            this@MainActivity,                   // Context (burada MainActivity)
//                            "Şehir bulunamadı",     // Gösterilecek mesaj
//                            Toast.LENGTH_SHORT      // Süre
//                        ).show()
//                    }
//            }}

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
                            updateWeatherUI(response)

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
    private fun updateWeatherUI(response: WeatherResponse) {
        val weatherMain = response.weather.first().main
        val animationRes = when (weatherMain) {
            "Clear" -> R.raw.sunny
            "Clouds" -> R.raw.cloudy
            "Rain" -> R.raw.rain
            "Drizzle" -> R.raw.rain
            "Thunderstorm" -> R.raw.thunderstorm
            "Snow" -> R.raw.snow
            else -> R.raw.cloudy
        }
        val backgroundRes = when (weatherMain) {
            "Clear" -> R.drawable.bg_sunny
            "Clouds" -> R.drawable.bg_cloudy
            "Rain" -> R.drawable.bg_rain
            "Drizzle" -> R.drawable.bg_rain
            "Thunderstorm" -> R.drawable.bg_storm
            "Snow" -> R.drawable.bg_snow
            else -> R.drawable.bg_cloudy
        }
        binding.rootLayout.setBackgroundResource(backgroundRes)
        binding.tvTemperature.text = "${response.main.temp}°C"
        binding.tvHumidity.text = "Humidity: ${response.main.humidity}%"
        binding.tvWindSpeed.text = "Wind: ${response.wind.speed} m/s"
        binding.tvDescription.text =
            response.weather[0].description.ilkHarfiBuyukYap()
        binding.tvCity.text = response.name
        //binding.tvFeelsLike.text =
            "Feels Like: ${response.main.feels_like}°C"
        binding.tvPressure.text =
            "Pressure: ${response.main.pressure} hPa"

        binding.weatherAnimation.setAnimation(animationRes)
        binding.weatherAnimation.playAnimation()

       // binding.progressBar.visibility = View.GONE
       // binding.weatherUI.visibility = View.VISIBLE
    }
}
