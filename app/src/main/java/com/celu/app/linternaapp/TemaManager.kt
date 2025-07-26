package com.celu.app.linternaapp

import android.content.Context

object TemaManager {
    fun obtenerRecursosDelTema(context: Context): EstiloTema {
        val attrs = intArrayOf(
            R.attr.fondoTema,
            R.attr.botonOn,
            R.attr.botonOff,
            R.attr.fondoCrud,
            R.attr.backgroundBotonesCrud
        )
        val a = context.obtainStyledAttributes(attrs)
        val estilo = EstiloTema(
            fondo = a.getResourceId(0, 0),
            botonOn = a.getResourceId(1, 0),
            botonOff = a.getResourceId(2, 0),
            fondoCrud = a.getResourceId(3, 0),
            fondoBotonesCrud = a.getResourceId(4, 0)
        )
        a.recycle()
        return estilo
    }
}
