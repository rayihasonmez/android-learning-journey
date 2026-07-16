package com.rayiha.rabbitgame

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rayiha.rabbitgame.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val random = Random()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : CountDownTimer(15000, 500) {
            override fun onTick(p0: Long) {
                binding.timeView.text = "Time: ${p0 / 1000}"

                val maxX = binding.parent.width - binding.imageView.width
                val maxY = binding.parent.height - binding.imageView.height

                val x = random.nextInt(maxX)
                val y = random.nextInt(maxY)

                binding.imageView.x = x.toFloat()
                binding.imageView.y = y.toFloat()


            }

            override fun onFinish() {
                binding.timeView.text = "Time: 0"
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game over")
                alert.setMessage("Do you wanna play again?")
                alert.setNegativeButton("No") { dialog, _ ->
                finish()
                }
                alert.setPositiveButton("Yes") { dialog, _ ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.show()
            }
        }.start()

        var score=0
        binding.imageView.setOnClickListener {
            score++
            binding.scoreView.text = "Score: $score"
        }
    }
}
