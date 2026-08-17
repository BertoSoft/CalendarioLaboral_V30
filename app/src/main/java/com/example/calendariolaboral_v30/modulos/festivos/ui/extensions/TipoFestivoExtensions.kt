package com.example.calendariolaboral_v30.modulos.festivos.ui.extensions

import androidx.annotation.StringRes
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo

//  Esto es una funcion de extension
@StringRes
fun TipoFestivo.toStringRes(): Int = when(this){
    TipoFestivo.NACIONAL -> R.string.tipo_nacional
    TipoFestivo.AUTONOMICO -> R.string.tipo_autonomico
    TipoFestivo.LOCAL -> R.string.tipo_local
    TipoFestivo.EXCESO_JORNADA -> R.string.tipo_exceso
    TipoFestivo.VACACIONES -> R.string.tipo_vacaciones
    TipoFestivo.CONVENIO -> R.string.tipo_convenio
}

fun String.toTipoFestivo(): TipoFestivo {
    return try {
        // valueOf busca la coincidencia exacta con el nombre del código (ej: "NACIONAL")
        TipoFestivo.valueOf(this.uppercase().trim())
    } catch (e: IllegalArgumentException) {
        // En caso de que el texto no coincida con ningún enum, devuelve uno por defecto
        TipoFestivo.NACIONAL
    }
}

