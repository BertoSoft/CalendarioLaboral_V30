package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.repository.VacacionesDetalleRepository

class VacacionesDetalleUseCase(
    private val vacacionesDetalleRepository: VacacionesDetalleRepository
) {

    suspend fun getDiasVacasPendientesUseCasse(): List<DatosVacasPendientes>{
        return vacacionesDetalleRepository.getDiasVacasPendientes()
    }

    suspend fun initVacasPendientesUseCase(lista: List<DatosVacasPendientes>): Boolean{
        return vacacionesDetalleRepository.initVacasPendientes(lista)
    }

}