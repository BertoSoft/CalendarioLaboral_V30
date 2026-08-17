package com.example.calendariolaboral_v30.core.data

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import kotlin.toString

class miSqliteHelper(miContexto: Context): SQLiteOpenHelper(
    miContexto,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(p0: SQLiteDatabase?) {
        // Tabla de festivos
        p0?.execSQL("""
            CREATE TABLE festivos (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha TEXT NOT NULL, 
                tipo_festivo TEXT NOT NULL
            )
        """.trimIndent())

        // Tabla de vacaciones
        p0?.execSQL("""
            CREATE TABLE vacaciones (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha_inicio TEXT NOT NULL,
                fecha_final TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        p0?.execSQL("DROP TABLE IF EXISTS festivos")
        p0?.execSQL("DROP TABLE IF EXISTS vacaciones")
        onCreate(p0)
    }

    //###################################################################3
    // Funciones de miSqliteHelper
    //##################################################################
    fun existeFestivo(dato: DatosFestivos): Int{
        var _id = -1
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM festivos", null)
        val utils = Utils();

        if(cursor.moveToFirst()){
            val colId = cursor.getColumnIndex("_id")
            val colFecha = cursor.getColumnIndex("fecha")
            while (!cursor.isAfterLast){
                val idDb = cursor.getInt(colId)
                val strFecha = cursor.getString(colFecha)
                val fecha = utils.fromFechaCOrtaToLocalDate(strFecha)

                if(fecha == dato.fecha){
                    _id = idDb
                    break
                }
                cursor.moveToNext()
            }
        }
        cursor.close()
        return  _id
    }

    fun getALlFestivos(strAno: String): List<DatosFestivos>{
        val lista = mutableListOf<DatosFestivos>()
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM festivos", null)
        val utils = Utils()

        if(cursor.moveToFirst()){
            val colId = cursor.getColumnIndex("_id")
            val colFecha = cursor.getColumnIndex("fecha")
            val colTipo = cursor.getColumnIndex("tipo_festivo")
            while (!cursor.isAfterLast){
                val strFecha = cursor.getString(colFecha)
                val strTipo = cursor.getString(colTipo)
                val _id = cursor.getInt(colId)

                val fecha = utils.fromFechaCOrtaToLocalDate(strFecha)
                val tipo = TipoFestivo.valueOf(strTipo)

                lista.add(DatosFestivos(
                    _id,
                    fecha,
                    tipo
                ))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return  lista.filter { festivo ->
            festivo.fecha.year.toString() == strAno
        }
    }

    fun setFestivo(id: Int, dato: DatosFestivos): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val strFecha = Utils().fromLocalDatetoFechaCorta(dato.fecha)
        val strTipo = dato.tipo.toString()
        val valores = ContentValues().apply {
            put("fecha", strFecha)
            put("tipo_festivo", strTipo)
        }
        return try {
            if(id < 0){
                db.insert("festivos", null, valores) != -1L
            }
            else{
                db.update("festivos", valores, "_id = ?", arrayOf(id.toString())) > 0
            }
        }
        catch (e: Exception){
            false
        }
    }

    //######################################################################
    // Nombre de la Base De Datos y Version
    //###################################################################3
    companion object {
        private const val DATABASE_NAME = "calendario.db"
        private const val DATABASE_VERSION = 1
    }
}