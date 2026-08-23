package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.repository.VacacionesDetalleRepository

class VacacionesDetalleRepositoryImpl(
    private val dbHelper: MiSqliteHelper
): VacacionesDetalleRepository {
}