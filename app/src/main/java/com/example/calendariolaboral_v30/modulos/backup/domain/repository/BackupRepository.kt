package com.example.calendariolaboral_v30.modulos.backup.domain.repository

import android.net.Uri

interface BackupRepository {

    suspend fun saveBackup(uri: Uri): Boolean
    suspend fun readBackup(uri: Uri): Boolean
}