package com.example.calendariolaboral_v30.modulos.festivos.ui.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos

//  Esto es una funcion de extension
@StringRes
fun TipoFestivos.toStringRes(): Int = when(this){
    TipoFestivos.NACIONAL -> R.string.tipo_nacional
    TipoFestivos.AUTONOMICO -> R.string.tipo_autonomico
    TipoFestivos.LOCAL -> R.string.tipo_local
    TipoFestivos.EXCESO_JORNADA -> R.string.tipo_exceso
    TipoFestivos.CONVENIO -> R.string.tipo_convenio
}

@DrawableRes
fun TipoFestivos.toImagen(): Int = when(this){
    TipoFestivos.NACIONAL -> R.drawable.spain
    TipoFestivos.AUTONOMICO -> R.drawable.galicia
    TipoFestivos.LOCAL -> R.drawable.santiago
    TipoFestivos.EXCESO_JORNADA -> R.drawable.logo
    TipoFestivos.CONVENIO -> R.drawable.cig
}

fun String.toTipoFestivo(): TipoFestivos {
    return try {
        var str = this.uppercase().trim()
        if(str == "EXCESO DE JORNADA") str = "EXCESO_JORNADA"
        TipoFestivos.valueOf(str)
    } catch (e: IllegalArgumentException) {
        // En caso de que el texto no coincida con ningún enum, devuelve uno por defecto
        TipoFestivos.NACIONAL
    }
}

