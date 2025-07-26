package com.celu.app.linternaapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Iniciar animación
        val imagen = findViewById<ImageView>(R.id.imagenSplash)
        imagen.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))

        // Ir al Main después de la animación
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // duración total: 2 segundos
    }
}
