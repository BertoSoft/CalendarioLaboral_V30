package com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase

import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository

class VacacionesUseCase(private val repository: VacacionesRepository) {

    suspend fun getAllVacacionesUseCase(): List<DatosVacaciones>{
        val lista = repository.getAllVacaciones()
        return lista.sortedBy { it.strFechaInicio }
    }

    suspend fun isFechasValidas(dato: DatosVacaciones): Boolean {
        if(dato.strFechaInicio == "" || dato.strFechaFinal == "" ) return false

        val utils = Utils()
        val fechaInicio = utils.fromFechaCortaToLocalDate(dato.strFechaInicio)
        val fechaFinal = utils.fromFechaCortaToLocalDate(dato.strFechaFinal)
        if(fechaFinal.isBefore(fechaInicio)){
            return false
        }
        return true
    }
}