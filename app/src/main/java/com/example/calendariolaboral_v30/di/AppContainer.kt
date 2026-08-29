package com.example.calendariolaboral_v30.di

// Importaciones cruzadas perfectas desde la raíz hacia abajo:
import android.content.Context
import com.example.calendariolaboral_v30.core.data.DatabaseIO
import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.core.data.repositoryimpl.BackupRepositoryImpl
import com.example.calendariolaboral_v30.core.data.repositoryimpl.ExcesosRepositoryImpl
import com.example.calendariolaboral_v30.core.data.repositoryimpl.FestivosRepositoryImpl
import com.example.calendariolaboral_v30.core.data.repositoryimpl.VacacionesRepositoryImpl
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository
import com.example.calendariolaboral_v30.modulos.backup.domain.usecase.BackupUseCase
import com.example.calendariolaboral_v30.modulos.calendario.domain.usecase.CalendarioUseCase
import com.example.calendariolaboral_v30.modulos.excesos.domain.repository.ExcesosRepository
import com.example.calendariolaboral_v30.modulos.excesos.domain.usecase.ExcesosUseCase
import com.example.calendariolaboral_v30.modulos.festivos.domain.repository.FestivosRepository
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.repository.VacacionesRepository
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase.VacacionesDetalleUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppContainer(private val miContexto: Context) {

    private val sqliteHelper: MiSqliteHelper by lazy {
        MiSqliteHelper(miContexto)
    }
    val coroutine: CoroutineDispatcher by lazy {
        Dispatchers.IO
    }

    val databaseIO: DatabaseIO by lazy {
        DatabaseIO(miContexto)
    }

    val utils: Utils by lazy {
        Utils()
    }

    // Festivos
    val festivosRepository: FestivosRepository by lazy {
        FestivosRepositoryImpl(sqliteHelper, coroutine)
    }
    val festivosUseCase: FestivosUseCase by lazy {
        FestivosUseCase(festivosRepository)
    }

    // Vacaciones
    val vacacionesRepository: VacacionesRepository by lazy {
        VacacionesRepositoryImpl(sqliteHelper, coroutine)
    }
    val vacacionesUseCase: VacacionesUseCase by lazy {
        VacacionesUseCase(vacacionesRepository, festivosRepository)
    }

    // Vacaciones detalle
    val vacacionesDetalleUseCase: VacacionesDetalleUseCase by lazy {
        VacacionesDetalleUseCase(vacacionesUseCase)
    }

    //Backup
    val backupRepository: BackupRepository by lazy {
        BackupRepositoryImpl(sqliteHelper, databaseIO, coroutine)
    }
    val backupUseCase: BackupUseCase by lazy {
        BackupUseCase(backupRepository)
    }


    // Excesos
    val excesosRepository: ExcesosRepository by lazy {
        ExcesosRepositoryImpl(sqliteHelper, coroutine)
    }
    val excesosUseCase: ExcesosUseCase by lazy {
        ExcesosUseCase(festivosRepository)
    }

    // Calendario
    val calendarioUseCase: CalendarioUseCase by lazy {
        CalendarioUseCase(festivosRepository, vacacionesRepository)
    }



}