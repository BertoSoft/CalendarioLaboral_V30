package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.repository.VacacionesDetalleRepository

class VacacionesDetalleRepositoryImpl(
    private val dbHelper: MiSqliteHelper
): VacacionesDetalleRepository {

    override suspend fun getDiasVacasPendientes(): List<DatosVacasPendientes>{
        return dbHelper.getDiasVacasPendientes()
    }

    override suspend fun initVacasPendientes(lista: List<DatosVacasPendientes>): Boolean {
        return dbHelper.initVacasPendientes(lista)
    }
}