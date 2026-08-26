package com.example.calendariolaboral_v30.core.data.repositoryimpl

import android.content.Context
import android.net.Uri
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository

class BackupRepositoryImpl(private val miContexto: Context): BackupRepository {

    override suspend fun saveBackup(uri: Uri): Boolean {



        return true
    }

    override suspend fun readBackup(uri: Uri): Boolean {



        return true
    }
}