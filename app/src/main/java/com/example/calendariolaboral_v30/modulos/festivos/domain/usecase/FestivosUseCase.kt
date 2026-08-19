package com.example.calendariolaboral_v30.modulos.festivos.domain.usecase

import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository


class FestivosUseCase(private val repository: FestivosRepository) {

    suspend fun getAllFestivosUseCase(strAno: String): List<DatosFestivos> {
        val lista: List<DatosFestivos> = repository.getAllFestivos(strAno)
        return lista.sortedBy { it.fecha }
    }

    suspend fun existeFestivoUseCase(dato: DatosFestivos): Int{
        return repository.existeFestivo(dato)
    }

    suspend fun setFestivoUseCase(dato: DatosFestivos): Boolean{
        val id = existeFestivoUseCase(dato)
        val datoNuevo = dato.copy(id = id)
        return repository.setFestivo(datoNuevo)
    }

    suspend fun  delFestivoUseCase(dato: DatosFestivos): Boolean{
        return repository.delFestivos(dato)
    }
}