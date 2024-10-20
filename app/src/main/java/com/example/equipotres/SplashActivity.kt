package com.example.equipotres

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Simular un retraso de 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Iniciar MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000) // 5000 milisegundos = 5 segundos
    }
}
