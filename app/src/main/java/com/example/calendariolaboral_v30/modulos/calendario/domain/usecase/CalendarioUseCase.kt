package com.example.calendariolaboral_v30.modulos.calendario.domain.usecase

import com.example.calendariolaboral_v30.modulos.calendario.domain.model.DatosCalendario
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CalendarioUseCase(
    private val festivosRepository: FestivosRepository,
    private val vacacionesRepository: VacacionesRepository
) {

    suspend fun getListaMesUseCase(mes: Int, ano: Int): List<DatosCalendario>{
        val listaDias = mutableListOf<DatosCalendario>()
        val mesAno = YearMonth.of(ano, mes)
        val diasMes = mesAno.lengthOfMonth()
        val strAno = ano.toString()
        val listaFestivos = festivosRepository.getAllFestivos(strAno)
        val listaVacaciones = vacacionesRepository.getAllVacaciones(strAno)

        val listaFestivosMes = listaFestivos.filter{ it.fecha.monthValue == mes }
        val listaVacacionesMes = listaVacaciones.filter { it.fecha_inicio.monthValue == mes }

        for(dia in 1 .. diasMes){
            val fecha = LocalDate.of(ano, mes, dia)
            val diaSemana = fecha.dayOfWeek
            val isSabado = diaSemana == DayOfWeek.SATURDAY
            val isDomingo = diaSemana == DayOfWeek.SUNDAY

            val festivoDia = listaFestivosMes.find { it.fecha == fecha }
            val isNacional = (festivoDia?.tipo == TipoFestivos.NACIONAL) ?: false
            val isAutonomico = festivoDia?.tipo == TipoFestivos.AUTONOMICO
            val isLocal = festivoDia?.tipo == TipoFestivos.LOCAL
            val isConvenio = festivoDia?.tipo == TipoFestivos.CONVENIO

            val isVacaciones = listaVacacionesMes.any { !fecha.isBefore(it.fecha_inicio) && !fecha.isAfter(it.fecha_final) }

            listaDias.add(
                DatosCalendario(
                    fecha,
                    isNacional,
                    isAutonomico,
                    isLocal,
                    isConvenio,
                    isVacaciones,
                    isSabado,
                    isDomingo,
                )
            )
        }
        return listaDias
    }
}