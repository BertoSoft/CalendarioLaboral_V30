package com.example.calendariolaboral_v30.modulos.backup.domain.usecase

import android.net.Uri
import com.example.calendariolaboral_v30.modulos.backup.domain.repository.BackupRepository

class BackupUseCase(private val backupRepository: BackupRepository) {

    suspend fun guardarCopia(uri: Uri): Boolean{
        return backupRepository.saveBackup(uri)
    }

    suspend fun abrirCopia(uri: Uri): Boolean{
        return backupRepository.readBackup(uri)
    }
}