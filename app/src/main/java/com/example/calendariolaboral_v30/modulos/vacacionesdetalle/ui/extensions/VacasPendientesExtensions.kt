package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.extensions

// Esto es un colector de funciones de extension

fun Int.toDias(): String{
    return when{
        this == 1 -> "$this Día."
        this > 1 -> "$this Días."
        else -> "-- Días"
    }
}
