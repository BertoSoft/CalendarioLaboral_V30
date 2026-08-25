package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase

import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes

class VacacionesDetalleUseCase(
    private val vacacionesUseCase: VacacionesUseCase
) {

    suspend fun getDatosUseCase(ano_actual: Int): DatosVacasPendientes{
        var diasAtrasados = 9 // Dias iniciales de 2021

        for(ano in 2022 until ano_actual){
            val diasDisfrutados = getDiasDisfrutados(ano)
            diasAtrasados += (22 - diasDisfrutados)
        }
        val lista = vacacionesUseCase.getAllVacacionesUseCase(ano_actual.toString())
        val diasDisfrutados = lista.sumOf { it.total_dias }
        val diasPendientes = (diasAtrasados + 22) - diasDisfrutados

        return DatosVacasPendientes(
            lista = lista,
            dias_atrasados = diasAtrasados,
            dias_disfrutados = diasDisfrutados,
            dias_pendientes = diasPendientes
        )
    }

    private suspend fun getDiasDisfrutados(ano_actual: Int): Int{
        val lista = vacacionesUseCase.getAllVacacionesUseCase(ano_actual.toString())
        return lista.sumOf { it.total_dias }
    }


}