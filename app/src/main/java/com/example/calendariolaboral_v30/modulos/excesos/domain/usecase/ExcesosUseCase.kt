package com.example.calendariolaboral_v30.modulos.excesos.domain.usecase

import com.example.calendariolaboral_v30.modulos.excesos.domain.models.DatosExceso
import com.example.calendariolaboral_v30.modulos.excesos.domain.repository.ExcesosRepository
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import java.time.DayOfWeek
import java.time.LocalDate

class ExcesosUseCase(
    private val festivosRepository: FestivosRepository
) {

    suspend fun getDatosUseCase(ano: Int): DatosExceso{
        val lista = festivosRepository.getAllFestivos(ano.toString())
        return DatosExceso(
            sabados = getSabados(ano),
            domingos = getDomingos(ano),
            lista.count{ it.tipo == TipoFestivos.NACIONAL },
            lista.count{ it.tipo == TipoFestivos.AUTONOMICO },
            lista.count{ it.tipo == TipoFestivos.LOCAL },
            lista.count{ it.tipo == TipoFestivos.CONVENIO }
        )
    }

    private fun getSabados(ano: Int): Int{
        var sabados = 0
        var fecha = LocalDate.of(ano, 1, 1)
        while (fecha.dayOfWeek != DayOfWeek.SATURDAY){
            fecha = fecha.plusDays(1)
        }
        while (fecha.year == ano){
            sabados ++
            fecha = fecha.plusWeeks(1)
        }
        return sabados
    }

    private fun getDomingos(ano: Int): Int{
        var domingos = 0
        var fecha = LocalDate.of(ano, 1, 1)
        while (fecha.dayOfWeek != DayOfWeek.SUNDAY){
            fecha = fecha.plusDays(1)
        }
        while (fecha.year == ano){
            domingos ++
            fecha = fecha.plusWeeks(1)
        }
        return domingos
    }

}