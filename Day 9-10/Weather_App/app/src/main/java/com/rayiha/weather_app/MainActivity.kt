package com.rayiha.weather_app

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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    fun String.ilkHarfiBuyukYap(): String {
        if (this.isEmpty()) return this
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
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
    }
