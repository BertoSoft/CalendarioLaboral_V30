package com.example.calendariolaboral_v30.di

// Importaciones cruzadas perfectas desde la raíz hacia abajo:
import android.content.Context
import com.example.calendariolaboral_v30.core.data.miSqliteHelper
import com.example.calendariolaboral_v30.core.data.FestivosRepositoryImpl
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase

class AppContainer(private val context: Context) {

    private val sqliteHelper: miSqliteHelper by lazy {
        miSqliteHelper(context)
    }

    val festivosRepository: FestivosRepository by lazy {
        FestivosRepositoryImpl(sqliteHelper)
    }

    val festivosUseCase: FestivosUseCase by lazy {
        FestivosUseCase(festivosRepository)
    }
}