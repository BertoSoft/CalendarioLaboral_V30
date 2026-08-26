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
           withContext(Dispatchers.IO){
               // Dentro de tu bloque withContext(Dispatchers.IO):
               miContexto.contentResolver.openOutputStream(uri, "wt")?.use { outputStream ->
                   fileOrigen.inputStream().use { inputStream ->
                       inputStream.copyTo(outputStream)
                   }
                   outputStream.flush()
               } ?: throw IOException("No se pudo abrir el stream...")            }
            true
        }
        catch (e: Exception){
            false
        }
    }

    suspend fun readBackup(uri: Uri): Boolean{
        val fileDestino: File = miContexto.getDatabasePath("calendario.db")
        return try {
            withContext(Dispatchers.IO){
                miContexto.contentResolver.openInputStream(uri)?.use { inputStream ->
                    fileDestino.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: throw IOException("Error al copiar el archivo...")
                true
            }
        }
        catch (e: Exception){
            false
        }
    }


}