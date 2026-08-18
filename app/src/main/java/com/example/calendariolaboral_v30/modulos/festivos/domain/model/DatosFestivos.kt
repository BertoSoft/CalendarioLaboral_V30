package com.example.calendariolaboral_v30.modulos.festivos.domain.model

import java.time.LocalDate

data class DatosFestivos(
    var id: Int,              // Identificador único para Room y RecyclerView
    val fecha: LocalDate,     // Campo 1: Fecha (ej: "2026-12-25" o en formato Date/Long)
    val tipo: TipoFestivo     // Campo 2: Tipo estructurado mediante un Enum
)
enum class TipoFestivo {
    NACIONAL,
    AUTONOMICO,
    LOCAL,
    EXCESO_JORNADA,
    CONVENIO
}