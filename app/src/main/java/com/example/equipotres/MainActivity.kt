package com.example.equipotres

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.equipotres.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countdownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        // Inicializar el sonido de fondo (opcional)
        // val mediaPlayer = MediaPlayer.create(this, R.raw.your_sound_file)
        // mediaPlayer.start()

        // Iniciar el contador regresivo
        startCountdown()

        // Configurar el botón "Presióname" para que parpadee
        makeButtonBlink(binding.pressMeButton)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Cierra todas las actividades y sale de la aplicación
    }

    private fun startCountdown() {
        binding.countdownText.text = "3"

        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                binding.countdownText.text = secondsLeft.toString()
            }

            override fun onFinish() {
                binding.countdownText.text = "0"
            }
        }.start()
    }

    private fun makeButtonBlink(button: Button) {
        val animator = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 500
        animator.start()
    }
}
