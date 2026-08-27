package com.example.calendariolaboral_v30.modulos.excesos.domain.usecase

import com.example.calendariolaboral_v30.modulos.excesos.domain.models.DatosExceso
import com.example.calendariolaboral_v30.modulos.excesos.domain.repository.ExcesosRepository
import java.time.DayOfWeek
import java.time.LocalDate

class ExcesosUseCase(
    private val excesosRepository: ExcesosRepository
) {

    suspend fun getDatosUseCase(strAno: String): DatosExceso{
        val sabados = getSabados(strAno)

        return DatosExceso(
            sabados,
            0,
            0,
            0,
            0,
            0
        )
    }

    private fun getSabados(strAno: String): Int{
        var sabados = 0
        var fecha = LocalDate.of(strAno.toInt(), 1, 1)
        while (fecha.dayOfWeek != DayOfWeek.SATURDAY){
            fecha = fecha.plusDays(1)
        }
        while (fecha.year == strAno.toInt()){
            sabados ++
            fecha = fecha.plusWeeks(1)
        }
        return sabados
    }
}