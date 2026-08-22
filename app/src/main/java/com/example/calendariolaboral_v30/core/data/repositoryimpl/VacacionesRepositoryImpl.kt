package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository

class VacacionesRepositoryImpl (
    private val dbHelper: MiSqliteHelper
): VacacionesRepository{

    override suspend fun getAllVacaciones(strAno: String): List<DatosVacaciones>{
        return dbHelper.getAllVacaciones(strAno)
    }

    override suspend fun existeVacaciones(dato: DatosVacaciones): Int {
        return dbHelper.existeVacaciones(dato)
    }

    override suspend fun setVacaciones(dato: DatosVacaciones): Boolean {
        return dbHelper.setVacaciones(dato)
    }
}