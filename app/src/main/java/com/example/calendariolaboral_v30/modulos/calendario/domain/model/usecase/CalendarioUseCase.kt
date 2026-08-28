package com.example.calendariolaboral_v30.modulos.calendario.domain.model.usecase

import com.example.calendariolaboral_v30.modulos.calendario.domain.model.DatosCalendario
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository

class CalendarioUseCase(
    festivosRepository: FestivosRepository,
    vacacionesRepository: VacacionesRepository
) {

    suspend fun getListaMesUseCase(mes: Int, ano: Int): List<DatosCalendario>{
        val lista: MutableList<DatosCalendario>

        return emptyList<DatosCalendario>()
    }
}