package com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

interface VacacionesRepository {

    suspend fun getAllVacaciones(): List<DatosVacaciones>
}
