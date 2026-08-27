package com.example.calendariolaboral_v30.core.data.repositoryimpl

import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.excesos.domain.repository.ExcesosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExcesosRepositoryImpl(
    private val miSqliteHelper: MiSqliteHelper,
    private val coroutine: CoroutineDispatcher
): ExcesosRepository {

}