package com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

interface VacacionesRepository {

    suspend fun getAllVacaciones(strAno: String): List<DatosVacaciones>
    suspend fun existeVacaciones(dato: DatosVacaciones): Int
    suspend fun  setVacaciones(dato: DatosVacaciones): Boolean
}
