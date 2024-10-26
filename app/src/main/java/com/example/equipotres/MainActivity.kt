package com.example.equipotres

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
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
    private lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mediaPlayer = MediaPlayer.create(this, R.raw.sound_main)
        mediaPlayer.isLooping = true // Hace que el sonido se repita en bucle
        mediaPlayer.start() // Inicia el sonido de fondo

        // Iniciar el contador regresivo
        startCountdown()

        // Configurar el botón "Presióname" para que parpadee
        makeButtonBlink(binding.bButton)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Cierra todas las actividades y sale de la aplicación
    }

    private fun startCountdown() {
        binding.contador.text = "3"

        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                binding.contador.text = secondsLeft.toString()
            }

            override fun onFinish() {
                binding.contador.text = "0"
                binding.contador.visibility = View.GONE
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
    override fun onPause() {
        super.onPause()
        mediaPlayer.pause() // Pausa el sonido si la actividad se pausa
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start() // Reanuda el sonido cuando la actividad vuelve a primer plano
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Libera los recursos del MediaPlayer
    }
}
