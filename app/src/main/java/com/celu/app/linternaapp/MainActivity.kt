package com.celu.app.linternaapp

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isOn = false
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var sosActivo = false
    private lateinit var hiloSOS: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("PreferenciasTemas", MODE_PRIVATE)
        setTheme(ConfiguracionActivity.obtenerTemaSeleccionado(prefs))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val (fondo, botonOn, botonOff) = TemaManager.obtenerRecursosDelTema(this)

        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        rootLayout.setBackgroundResource(fondo)

        val btnOn = findViewById<ImageButton>(R.id.btnLinternaOn)
        val btnOff = findViewById<ImageButton>(R.id.btnLinternaOff)

        btnOn.setImageResource(botonOn)
        btnOff.setImageResource(botonOff)

        // ✅ Usamos un solo bloque typedArray
        val typedArray = theme.obtainStyledAttributes(
            ConfiguracionActivity.obtenerTemaSeleccionado(prefs),
            R.styleable.TemaPersonalizado
        )

        val fondoLinterna = typedArray.getResourceId(
            R.styleable.TemaPersonalizado_backgroundBotonLinterna,
            0
        )

        val fondoBotonesCrud = typedArray.getResourceId(
            R.styleable.TemaPersonalizado_backgroundBotonesCrud,
            0
        )

        typedArray.recycle() // Se hace solo una vez al final

        if (fondoLinterna != 0) {
            btnOn.setBackgroundResource(fondoLinterna)
            btnOff.setBackgroundResource(fondoLinterna)
        }

        if (fondoBotonesCrud != 0) {
            findViewById<ImageButton>(R.id.btnLow).setBackgroundResource(fondoBotonesCrud)
            findViewById<ImageButton>(R.id.btnConfig).setBackgroundResource(fondoBotonesCrud)
            findViewById<ImageButton>(R.id.btnHigh).setBackgroundResource(fondoBotonesCrud)
            findViewById<ImageButton>(R.id.btnSalir).setBackgroundResource(fondoBotonesCrud)
        }

        inicializarLinterna()
        configurarBotones()
    }


    private fun inicializarLinterna() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList.first {
                cameraManager.getCameraCharacteristics(it)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun configurarBotones() {
        val btnLinternaOn = findViewById<ImageButton>(R.id.btnLinternaOn)
        val btnLinternaOff = findViewById<ImageButton>(R.id.btnLinternaOff)
        val btnConfig = findViewById<ImageButton>(R.id.btnConfig)
        val btnSalir = findViewById<ImageButton>(R.id.btnSalir)
        val btnSOS = findViewById<ImageButton>(R.id.btnHigh)
        val btnStop = findViewById<ImageButton>(R.id.btnLow)

        btnLinternaOff.setOnClickListener {
            encenderLinterna()
            animarBoton(btnLinternaOff, subir = false)
            btnLinternaOff.visibility = View.GONE
            btnLinternaOn.visibility = View.VISIBLE
        }

        btnLinternaOn.setOnClickListener {
            apagarLinterna()
            animarBoton(btnLinternaOn, subir = true)
            btnLinternaOn.visibility = View.GONE
            btnLinternaOff.visibility = View.VISIBLE
        }


        btnSOS.setOnClickListener { emitirSOS() }
        btnStop.setOnClickListener { detenerSOS() }

        btnConfig.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        btnSalir.setOnClickListener {
            finishAffinity()
        }
    }

    private fun encenderLinterna() {
        try {
            cameraManager.setTorchMode(cameraId, true)
            isOn = true
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun apagarLinterna() {
        try {
            cameraManager.setTorchMode(cameraId, false)
            isOn = false
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun emitirSOS() {
        sosActivo = true
        hiloSOS = Thread {
            try {
                val corto = 200L
                val largo = 600L
                val pausa = 150L

                while (sosActivo && !Thread.currentThread().isInterrupted) {
                    repetirFlash(3, corto, pausa) // S
                    Thread.sleep(400)
                    repetirFlash(3, largo, pausa) // O
                    Thread.sleep(400)
                    repetirFlash(3, corto, pausa) // S
                    Thread.sleep(1500)
                }
            } catch (e: InterruptedException) {
                // Silencio
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
        hiloSOS.start()
    }

    private fun detenerSOS() {
        sosActivo = false
        hiloSOS.interrupt()
        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun repetirFlash(veces: Int, duracion: Long, pausa: Long) {
        repeat(veces) {
            cameraManager.setTorchMode(cameraId, true)
            Thread.sleep(duracion)
            cameraManager.setTorchMode(cameraId, false)
            Thread.sleep(pausa)
        }
    }
    private fun animarBoton(boton: ImageButton, subir: Boolean) {
        val desplazamiento = if (subir) -80f else 0f
        boton.animate()
            .translationY(desplazamiento)
            .setDuration(300)
            .start()
    }

}
