package com.example.calendariolaboral_v30.modulos.vacaciones.domain.model

import java.time.LocalDate

data class DatosVacaciones(
    val id: Int,
    val FechaInicio: LocalDate,
    val FechaFinal: LocalDate,
)
