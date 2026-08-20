package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.miSqliteHelper
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository

class FestivosRepositoryImpl (
    private val dbHelper: miSqliteHelper
): FestivosRepository {

    override suspend fun getAllFestivos(strAno: String): List<DatosFestivos> {
       return dbHelper.getALlFestivos(strAno)
    }

    override suspend fun existeFestivo(dato: DatosFestivos): Int {
        return dbHelper.existeFestivo(dato)
    }

    override suspend fun setFestivo(dato: DatosFestivos): Boolean {
        return dbHelper.setFestivo(dato)
    }

    override suspend fun delFestivos(dato: DatosFestivos): Boolean {
        return dbHelper.delFestivo(dato)
    }
}