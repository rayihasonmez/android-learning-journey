package com.rayiha.storingdata

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rayiha.storingdata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = this.getSharedPreferences("com.rayiha.storingdata", Context.MODE_PRIVATE)

        val userAgePref = sharedPref.getInt("age",-1)
        if (userAgePref != -1)
        binding.textView.text= "Your age:  ${userAgePref}"
    }

    fun save(view: View){
        val myAge = binding.editText.text.toString().toIntOrNull()
        if(myAge!=null){
            binding.textView.text="Your age:  ${myAge}"
            sharedPref.edit().putInt("age",myAge).apply()
        }

    }
    fun delete(view: View){
            sharedPref.edit().remove("age").apply()
            binding.textView.text="Your age:"
    }


    }