package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.miSqliteHelper
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository

class VacacionesRepositoryImpl (
    private val dbHelper: miSqliteHelper
): VacacionesRepository{

    override suspend fun getAllVacaciones(): List<DatosVacaciones>{
        return dbHelper.getAllVacaciones()
    }
}