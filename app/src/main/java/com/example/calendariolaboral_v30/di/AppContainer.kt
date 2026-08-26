package com.example.calendariolaboral_v30.di

// Importaciones cruzadas perfectas desde la raíz hacia abajo:
import android.app.Application
import android.content.Context
import com.example.calendariolaboral_v30.core.data.DatabaseIO
import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.core.data.repositoryimpl.BackupRepositoryImpl
import com.example.calendariolaboral_v30.core.data.repositoryimpl.FestivosRepositoryImpl
import com.example.calendariolaboral_v30.core.data.repositoryimpl.VacacionesRepositoryImpl
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository
import com.example.calendariolaboral_v30.modulos.backup.domain.usecase.BackupUseCase
import com.example.calendariolaboral_v30.modulos.backup.ui.viewmodel.BackupViewModel
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase.VacacionesDetalleUseCase

class AppContainer(private val miContexto: Context) {

    private val sqliteHelper: MiSqliteHelper by lazy {
        MiSqliteHelper(miContexto)
    }

    val festivosRepository: FestivosRepository by lazy {
        FestivosRepositoryImpl(sqliteHelper)
    }
    val festivosUseCase: FestivosUseCase by lazy {
        FestivosUseCase(festivosRepository)
    }

    val vacacionesRepository: VacacionesRepository by lazy {
        VacacionesRepositoryImpl(sqliteHelper)
    }

    val vacacionesUseCase: VacacionesUseCase by lazy {
        VacacionesUseCase(vacacionesRepository, festivosRepository)
    }

    val vacacionesDetalleUseCase: VacacionesDetalleUseCase by lazy {
        VacacionesDetalleUseCase(vacacionesUseCase)
    }

    val backupRepository: BackupRepository by lazy {
        BackupRepositoryImpl(sqliteHelper, databaseIO)
    }

    val backupUseCase: BackupUseCase by lazy {
        BackupUseCase(backupRepository)
    }

    val backupRepositoryImpl: BackupRepositoryImpl by lazy {
        BackupRepositoryImpl(sqliteHelper, databaseIO)
    }

    val databaseIO: DatabaseIO by lazy {
        DatabaseIO(miContexto)
    }

    val utils: Utils by lazy {
        Utils()
    }
}