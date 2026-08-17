package com.example.calendariolaboral_v30.modulos.festivos.domain.repository

import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos

interface FestivosRepository {
    suspend fun getAllFestivos(strAno: String): List<DatosFestivos>
    suspend fun existeFestivo(dato: DatosFestivos): Int
    suspend fun setFestivo(id: Int, dato: DatosFestivos): Boolean
}
