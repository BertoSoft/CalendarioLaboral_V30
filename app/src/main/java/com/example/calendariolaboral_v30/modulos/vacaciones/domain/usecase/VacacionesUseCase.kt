package com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase

import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository
import java.security.PrivateKey
import java.time.DayOfWeek
import java.time.LocalDate

class VacacionesUseCase(
    private val vacacionesRepository: VacacionesRepository,
    private val festivosRepository: FestivosRepository
    ) {

    suspend fun getAllVacacionesUseCase(strAno: String): List<DatosVacaciones>{
        var dias_totales = 0
        val lista_sin_dias_totales = vacacionesRepository.getAllVacaciones(strAno)
        val lista_festivos = festivosRepository.getAllFestivos(strAno)
        var is_sabado = false
        var is_domingo = false
        var fecha: LocalDate
        val lista_con_dias_totales = lista_sin_dias_totales.map { vacaciones ->
            fecha = vacaciones.fecha_inicio
            dias_totales = 0
            while (fecha <= vacaciones.fecha_final) {
                is_sabado = false
                is_domingo = false
                if(fecha.dayOfWeek == DayOfWeek.SATURDAY) is_sabado = true
                if(fecha.dayOfWeek == DayOfWeek.SUNDAY) is_domingo = true
                if(!is_sabado && !is_domingo) {
                    val id = festivosRepository.existeFestivo(DatosFestivos(
                        -1,
                        fecha,
                        TipoFestivos.NACIONAL
                    ))
                    if(id < 0) dias_totales += 1
                }
                fecha = fecha.plusDays(1)
            }
            vacaciones.copy(total_dias = dias_totales)
        }
        return lista_con_dias_totales.sortedBy { it.fecha_inicio }
    }

    suspend fun isFechasValidas(dato: DatosVacaciones): Boolean {
        val utils = Utils()

        if(dato.fecha_final.isBefore(dato.fecha_inicio)){
            return false
        }
        return true
    }

    suspend fun existeVacacionesUseCase(dato: DatosVacaciones): Int{
        return vacacionesRepository.existeVacaciones(dato)
    }

    suspend  fun setVacacionesUseCase(dato: DatosVacaciones): Boolean {
        return vacacionesRepository.setVacaciones(dato)
    }
}