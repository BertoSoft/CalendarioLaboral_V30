package com.example.calendariolaboral_v30.modulos.excesos.ui.extensions

fun Int.toHoras(): String{
    return when{
        this == 1 -> "$this Hora."
        this > 1 -> "$this Horas."
        else -> "-- Hora"
    }
}
