package com.example.calendariolaboral_v30.core.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

class Utils {

    private val localeEspanol = Locale("es", "ES")
    private val formateadorLargo = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("EEEE, d 'de' MMMM 'de' yyyy")
        .toFormatter(localeEspanol)
    private val formateadorCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun fromLocalDateToCalendar(localDate: LocalDate): Calendar {
        return Calendar.getInstance().apply {
            set(
                localDate.year,
                localDate.monthValue - 1, // Restamos 1 por el índice cero de Calendar
                localDate.dayOfMonth
            )
        }
    }

    fun fromLocalDateToFechaLarga(localDate: LocalDate): String {
        return try {
            localDate.format(formateadorLargo)
        } catch (e: Exception) {
            "Fecha no válida"
        }
    }

    fun fromFechaLargaToLocalDate(strFecha: String): LocalDate{
        return try {
            val formateador = DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("EEEE, d 'de' MMMM 'de' yyyy")
                .toFormatter(localeEspanol)

            LocalDate.parse(strFecha.trim(), formateador)
        }
        catch (e: DateTimeParseException){
            LocalDate.now()
        }
    }

    fun fromLocalDateToFechaCorta(localDate: LocalDate): String {
        return try {
            localDate.format(formateadorCorto)
        } catch (e: Exception) {
            "Formato erróneo"
        }
    }

    fun fromCalendarToLocalDate(calendar: Calendar): LocalDate {
        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1, // Sumamos 1 por el índice cero de Calendar
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun fromFechaCortaToLocalDate(strFecha: String): LocalDate {
        return try {
            LocalDate.parse(strFecha.trim(), formateadorCorto)
        }
        catch (e: DateTimeParseException){
            LocalDate.now()
        }
    }
}