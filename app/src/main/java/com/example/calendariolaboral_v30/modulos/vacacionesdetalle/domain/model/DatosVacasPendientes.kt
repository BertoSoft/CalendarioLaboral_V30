package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

data class DatosVacasPendientes(
    val lista: List<DatosVacaciones> = emptyList(),
    val dias_atrasados: Int = 0,
    val dias_disfrutados: Int = 0,
    val dias_pendientes: Int = 0
)
