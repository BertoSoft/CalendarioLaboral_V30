package com.example.calendariolaboral_v30.modulos.vacaciones.domain.model

import java.time.LocalDate

data class DatosVacaciones(
    val id: Int,
    val fecha_inicio: LocalDate,
    val fecha_final: LocalDate,
    val total_dias: Int
)
