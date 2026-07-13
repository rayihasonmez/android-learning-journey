package com.rayiha.calc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ZoomButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rayiha.calc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var number1: EditText
//    private lateinit var number2: EditText
//    private lateinit var plusButton: Button
//    private lateinit var minusButton: Button
//    private lateinit var divisionButton: Button
//    private lateinit var asteriksButton: Button
//    private lateinit var resultText: TextView

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.asteriksButton.setOnClickListener {
            var number1= binding.number1.text.toString().toDoubleOrNull()
            var number2= binding.number2.text.toString().toDoubleOrNull()
            if(number1 != null && number2 != null){
                binding.resultText.text = "Result: ${number1*number2}"
            }
            else{
                binding.resultText.text ="gecerli sayı giriniz"
            }

        }

        binding.divisionButton.setOnClickListener {
            var number1= binding.number1.text.toString().toDoubleOrNull()
            var number2= binding.number2.text.toString().toDoubleOrNull()
            if(number1 != null && number2 != null){
                binding.resultText.text ="Result: ${number1/number2}"
            }
            else{
                binding.resultText.text ="gecerli sayı giriniz"
            }

        }

        binding.plusButton.setOnClickListener {
            var number1= binding.number1.text.toString().toDoubleOrNull()
            var number2= binding.number2.text.toString().toDoubleOrNull()
            if(number1 != null && number2 != null){
                binding.resultText.text ="Result: ${number1+number2}"
            }
            else{
                binding.resultText.text ="gecerli sayı giriniz"
            }

        }

        binding.minusButton.setOnClickListener {
            var number1= binding.number1.text.toString().toDoubleOrNull()
            var number2= binding.number2.text.toString().toDoubleOrNull()
            if(number1 != null && number2 != null){
                binding.resultText.text ="Result: ${number1-number2}"
            }
            else{
                binding.resultText.text ="gecerli sayı giriniz"
            }

        }



        }
}



