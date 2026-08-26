package com.example.calendariolaboral_v30.core.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class DatabaseIO(
    private val miContexto: Context
) {

    suspend fun saveBackup(uri: Uri): Boolean{
        val fileOrigen: File = miContexto.getDatabasePath("calendario.db")
        if(!fileOrigen.exists())return false
        // Coorrutina que grabara los datos de Calendario.db en el archivo Backup
        return try {
            val outputStream = miContexto.contentResolver.openOutputStream(uri)
                ?: throw IOException("No se pudo abrir el stream de destino para la URI proporcionada")
            withContext(Dispatchers.IO){
                fileOrigen.inputStream().copyTo(outputStream)
            }

            true
        }
        catch (e: Exception){
            false
        }
    }

    suspend fun readBackup(uri: Uri): Boolean{

        return true
    }


}