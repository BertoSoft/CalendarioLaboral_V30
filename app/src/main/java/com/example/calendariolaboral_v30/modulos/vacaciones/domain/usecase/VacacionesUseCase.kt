package com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase

import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository

class VacacionesUseCase(private val repository: VacacionesRepository) {

    suspend fun getAllVacacionesUseCase(strAno: String): List<DatosVacaciones>{
        val lista = repository.getAllVacaciones(strAno)
        return lista.sortedBy { it.FechaInicio }
    }

    suspend fun isFechasValidas(dato: DatosVacaciones): Boolean {
        val utils = Utils()

        if(dato.FechaFinal.isBefore(dato.FechaInicio)){
            return false
        }
        return true
    }

    suspend fun existeVacacionesUseCase(dato: DatosVacaciones): Int{
        return repository.existeVacaciones(dato)
    }

    suspend  fun setVacacionesUseCase(dato: DatosVacaciones): Boolean {
        return repository.setVacaciones(dato)
    }
}