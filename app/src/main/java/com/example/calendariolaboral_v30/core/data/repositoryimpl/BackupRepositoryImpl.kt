package com.example.calendariolaboral_v30.core.data.repositoryimpl

import android.net.Uri
import com.example.calendariolaboral_v30.core.data.DatabaseIO
import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BackupRepositoryImpl(
    private val miSqliteHelper: MiSqliteHelper,
    private val miDatabaseIO: DatabaseIO,
    private val coroutine: CoroutineDispatcher
    ): BackupRepository {

    override suspend fun saveBackup(uri: Uri): Boolean {
        return withContext(coroutine){ miDatabaseIO.saveBackup(uri)}
    }

    override suspend fun readBackup(uri: Uri): Boolean {
        return withContext(coroutine){
            miSqliteHelper.close()
            miDatabaseIO.readBackup(uri)
        }
    }
}