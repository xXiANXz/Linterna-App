package com.celu.app.linternaapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConfiguracionActivity : AppCompatActivity() {

    private val prefsFile = "PreferenciasTemas"
    private val keyTema = "TemaSeleccionado"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracion_activity)

        val botonesTemas = mapOf(
            R.id.btnTema1 to R.style.TemaMetalico,
            R.id.btnTema2 to R.style.TemaLED,
            R.id.btnTema3 to R.style.TemaCamuflado,
            R.id.btnTema4 to R.style.TemaFuturista,
            R.id.btnTema5 to R.style.TemaAnimal,
            R.id.btnTema6 to R.style.TemaAnimalPink
        )

        for ((idBoton, estiloTema) in botonesTemas) {
            findViewById<Button>(idBoton).setOnClickListener {
                guardarTemaSeleccionado(estiloTema)
                reiniciarApp()
            }
        }
    }

    private fun guardarTemaSeleccionado(estilo: Int) {
        val prefs = getSharedPreferences(prefsFile, MODE_PRIVATE)
        prefs.edit().putInt(keyTema, estilo).apply()
    }

    private fun reiniciarApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        fun obtenerTemaSeleccionado(prefs: SharedPreferences): Int {
            return prefs.getInt("TemaSeleccionado", R.style.TemaMetalico) // Tema por defecto
        }
    }
}

