package com.example.calendariolaboral_v30.modulos.festivos.domain.usecase

import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository


class FestivosUseCase(private val repository: FestivosRepository) {

    suspend fun getAllFestivosUseCase(strAno: String): List<DatosFestivos> {
        return repository.getAllFestivos(strAno)
    }

    suspend fun existeFestivoUseCase(dato: DatosFestivos): Int{
        return repository.existeFestivo(dato)
    }

    suspend fun setFestivoUseCase(dato: DatosFestivos){
        repository.setFestivo(existeFestivoUseCase(dato), dato)
    }
}