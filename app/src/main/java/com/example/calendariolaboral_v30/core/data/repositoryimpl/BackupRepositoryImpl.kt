package com.example.calendariolaboral_v30.core.data.repositoryimpl

import android.net.Uri
import com.example.calendariolaboral_v30.core.data.DatabaseIO
import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository

class BackupRepositoryImpl(
    private val miSqliteHelper: MiSqliteHelper,
    private val miDatabaseIO: DatabaseIO
    ): BackupRepository {

    override suspend fun saveBackup(uri: Uri): Boolean {
        return miDatabaseIO.saveBackup(uri)
    }

    override suspend fun readBackup(uri: Uri): Boolean {
        miSqliteHelper.close()
        return miDatabaseIO.readBackup(uri)
    }
}