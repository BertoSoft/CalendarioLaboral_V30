package com.example.calendariolaboral_v30.modulos.festivos.domain.model

import java.time.LocalDate

data class DatosFestivos(
    var id: Int,              // Identificador único para Room y RecyclerView
    val fecha: LocalDate,     // Campo 1: Fecha (ej: "2026-12-25" o en formato Date/Long)
    val tipo: TipoFestivos     // Campo 2: Tipo estructurado mediante un Enum
)
enum class TipoFestivos{
    NACIONAL,
    AUTONOMICO,
    LOCAL,
    EXCESO_JORNADA,
    CONVENIO
}