package com.example.calendariolaboral_v30.core.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes

class MiSqliteHelper(miContexto: Context): SQLiteOpenHelper(
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
        var id = -1
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM festivos", null)
        val utils = Utils();

        if(cursor.moveToFirst()){
            val colId = cursor.getColumnIndex("_id")
            val colFecha = cursor.getColumnIndex("fecha")
            while (!cursor.isAfterLast){
                val _id = cursor.getInt(colId)
                val strFecha = cursor.getString(colFecha)
                val fecha = utils.fromFechaCortaToLocalDate(strFecha)

                if(fecha == dato.fecha){
                    id = _id
                    break
                }
                cursor.moveToNext()
            }
        }
        cursor.close()
        return  id
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

                val fecha = utils.fromFechaCortaToLocalDate(strFecha)
                val tipo = TipoFestivos.valueOf(strTipo)

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

    fun setFestivo(dato: DatosFestivos): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val strFecha = Utils().fromLocalDateToFechaCorta(dato.fecha)
        val strTipo = dato.tipo.toString()
        val valores = ContentValues().apply {
            put("fecha", strFecha)
            put("tipo_festivo", strTipo)
        }
        return try {
            if(dato.id < 0){
                db.insert("festivos", null, valores) != -1L
            }
            else{
                db.update("festivos", valores, "_id = ?", arrayOf(dato.id.toString())) > 0
            }
        }
        catch (e: Exception){
            false
        }
    }

    fun delFestivo(dato: DatosFestivos): Boolean{
        val db = writableDatabase
        val where = "_id = ?"
        val arg = arrayOf(dato.id.toString())

        return try {
            val filasAfectadas = db.delete("festivos", where, arg)
            filasAfectadas > 0
        }
        catch (e: Exception){
            e.printStackTrace()
            false
        }
        finally {
            db.close()
        }
    }

    fun getAllVacaciones(strAno: String): List<DatosVacaciones>{
        val lista = mutableListOf<DatosVacaciones>()
        val utils = Utils()
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery("SELECT *FROM vacaciones", null)
        if(cursor.moveToFirst()){
            val colId = cursor.getColumnIndex("_id")
            val colFecha1 = cursor.getColumnIndex("fecha_inicio")
            val colFecha2 = cursor.getColumnIndex("fecha_final")
            while (!cursor.isAfterLast){
                val id = cursor.getInt(colId)
                val strFecha1 = cursor.getString(colFecha1)
                val strFecha2 = cursor.getString(colFecha2)
                val fecha_inicio = utils.fromFechaCortaToLocalDate(strFecha1)
                val fecha_final = utils.fromFechaCortaToLocalDate(strFecha2)

                lista.add(DatosVacaciones(
                    id,
                    fecha_inicio,
                    fecha_final,
                    -1
                ))

                cursor.moveToNext()
            }
            cursor.close()
        }

        return lista.filter { vacaciones ->
            vacaciones.fecha_inicio.year.toString() == strAno
        }
    }

    fun existeVacaciones(dato: DatosVacaciones): Int{
        var id = -1
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM vacaciones", null)
        val utils = Utils();

        if(cursor.moveToFirst()){
            val colId = cursor.getColumnIndex("_id")
            val colFecha1 = cursor.getColumnIndex("fecha_inicio")
            val colFecha2 = cursor.getColumnIndex("fecha_final")
            while (!cursor.isAfterLast){
                val _id = cursor.getInt(colId)
                val strFecha1 = cursor.getString(colFecha1)
                val strFecha2 = cursor.getString(colFecha2)
                val fecha_inicio = utils.fromFechaCortaToLocalDate(strFecha1)
                val fecha_final = utils.fromFechaCortaToLocalDate(strFecha2)
                // si la fecha inicial esta en el intervalo
                if(dato.fecha_inicio in fecha_inicio .. fecha_final ||
                    dato.fecha_final in fecha_inicio .. fecha_final
                    ){
                    id = cursor.getInt(colId)
                    break
                }
                cursor.moveToNext()
            }
            cursor.close()
        }
        return id
    }

    fun setVacaciones(dato: DatosVacaciones): Boolean{
        val db: SQLiteDatabase = writableDatabase
        val strFechaInicio = Utils().fromLocalDateToFechaCorta(dato.fecha_inicio) ?: ""
        val strFechaFinal = Utils().fromLocalDateToFechaCorta(dato.fecha_final) ?: ""

        val valores = ContentValues().apply {
            put("fecha_inicio", strFechaInicio)
            put("fecha_final", strFechaFinal)
        }
        return try {
            if(dato.id < 0){
                db.insert("vacaciones", null, valores) != -1L
            }
            else{
                db.update("vacaciones", valores, "_id = ?", arrayOf(dato.id.toString())) > 0
            }
        }
        catch (e: Exception){
            false
        }
    }

    fun delVacaciones(dato: DatosVacaciones): Boolean{
        val db = writableDatabase
        val where = "_id = ?"
        val arg = arrayOf(dato.id.toString())

        return try {
            val filasAfectadas = db.delete("vacaciones", where, arg)
            filasAfectadas > 0
        }
        catch (e: Exception){
            e.printStackTrace()
            false
        }
        finally {
            db.close()
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