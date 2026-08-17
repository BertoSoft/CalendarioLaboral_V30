package com.example.calendariolaboral_v30.core.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Utils {

    private val localeEspanol = Locale("es", "ES")
    private val formateadorLargo = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", localeEspanol)
    private val formateadorCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun fromLocalDatetoCalendar(localDate: LocalDate): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(
            localDate.year,
            localDate.monthValue - 1, // Restamos 1 por el índice cero de Calendar
            localDate.dayOfMonth
        )
        return calendar
    }

    fun fromLocalDatetoFechaLarga(localDate: LocalDate): String {
        return try {
            localDate.format(formateadorLargo)
        } catch (e: Exception) {
            "Fecha no válida"
        }
    }

    fun fromLocalDatetoFechaCorta(localDate: LocalDate): String {
        return try {
            // CORREGIDO: Ahora usa formateadorCorto correctamente
            localDate.format(formateadorCorto)
        } catch (e: Exception) {
            "Formato erróneo"
        }
    }

    fun fromCalendartoLocalDate(calendar: Calendar): LocalDate {
        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1, // Sumamos 1 por el índice cero de Calendar
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun fromFechaCOrtaToLocalDate(strFecha: String): LocalDate {
        val strAno = strFecha.substring(6, 10)
        val strMes = strFecha.substring(3, 5)
        val strDia = strFecha. substring(0,2)
        return LocalDate.of(strAno.toInt(), strMes.toInt(), strDia.toInt())
    }

}