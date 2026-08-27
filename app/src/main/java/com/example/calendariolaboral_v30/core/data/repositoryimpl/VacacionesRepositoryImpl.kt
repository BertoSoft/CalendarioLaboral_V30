package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class VacacionesRepositoryImpl (
    private val dbHelper: MiSqliteHelper,
    private val coroutine: CoroutineDispatcher
): VacacionesRepository{

    override suspend fun getAllVacaciones(strAno: String): List<DatosVacaciones>{
        return withContext(coroutine){
            dbHelper.getAllVacaciones(strAno)
        }    }

    override suspend fun existeVacaciones(dato: DatosVacaciones): Int {
        return withContext(coroutine){
            dbHelper.existeVacaciones(dato)
        }    }

    override suspend fun setVacaciones(dato: DatosVacaciones): Boolean {
        return withContext(coroutine){
            dbHelper.setVacaciones(dato)
        }    }

    override suspend fun delVacaciones(dato: DatosVacaciones): Boolean {
        return withContext(coroutine){
            dbHelper.delVacaciones(dato)
        }    }
}