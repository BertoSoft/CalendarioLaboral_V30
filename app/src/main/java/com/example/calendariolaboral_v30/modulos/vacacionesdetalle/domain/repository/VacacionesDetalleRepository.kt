package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.repository

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes

interface VacacionesDetalleRepository {

    suspend fun getDiasVacasPendientes(): List<DatosVacasPendientes>

    suspend fun initVacasPendientes(lista: List<DatosVacasPendientes>): Boolean
}