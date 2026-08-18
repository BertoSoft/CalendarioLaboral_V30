package com.example.calendariolaboral_v30.modulos.festivos.ui.extensions

import androidx.annotation.DrawableRes
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
    TipoFestivo.CONVENIO -> R.string.tipo_convenio
}

@DrawableRes
fun TipoFestivo.toImagen(): Int = when(this){
    TipoFestivo.NACIONAL -> R.drawable.spain
    TipoFestivo.AUTONOMICO -> R.drawable.galicia
    TipoFestivo.LOCAL -> R.drawable.santiago
    TipoFestivo.EXCESO_JORNADA -> R.drawable.logo
    TipoFestivo.CONVENIO -> R.drawable.cig
}

fun String.toTipoFestivo(): TipoFestivo {
    return try {
        var str = this.uppercase().trim()
        if(str == "EXCESO DE JORNADA") str = "EXCESO_JORNADA"
        TipoFestivo.valueOf(str)
    } catch (e: IllegalArgumentException) {
        // En caso de que el texto no coincida con ningún enum, devuelve uno por defecto
        TipoFestivo.NACIONAL
    }
}

