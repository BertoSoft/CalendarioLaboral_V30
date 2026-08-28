package com.example.calendariolaboral_v30.modulos.calendario.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.calendario.domain.model.DatosCalendario
import com.example.calendariolaboral_v30.modulos.calendario.domain.model.Meses
import com.example.calendariolaboral_v30.modulos.calendario.domain.model.usecase.CalendarioUseCase
import kotlinx.coroutines.launch
import java.security.PrivateKey
import java.time.LocalDate

data class CalendarioUiEstado(
    val isCarganado: Boolean = false,
    val msgError: String? = null,
    val listaMes: List<DatosCalendario>? = null,
    val mes: String? = null,
    val ano: String? = null
)

class CalendarioViewModel(
    private val utils: Utils,
    private val calendarioUseCase: CalendarioUseCase
): ViewModel() {

    private  val _estado = MutableLiveData<CalendarioUiEstado>(CalendarioUiEstado())
    val estado: LiveData<CalendarioUiEstado> get() = _estado

    fun spMesClick(strMes: String){
        val estadoOld = _estado.value ?: CalendarioUiEstado()
        val strAno = _estado.value?.ano ?: LocalDate.now().year.toString()

        val mes = Meses.entries.indexOfFirst { it.name == strMes } + 1
        val ano = strAno.toIntOrNull() ?: 2026

        _estado.value = estadoOld.copy(
            mes = strMes,
            ano = strAno
        )
        getListaDatosMes(mes, ano)
    }

    fun spAnoClick(strAno: String){
        val estadoOld = _estado.value ?: CalendarioUiEstado()
        val strMes = _estado.value?.mes ?: Meses.entries[LocalDate.now().monthValue -1].name

        val mes = Meses.entries.indexOfFirst { it.name == strMes } + 1
        val ano = strAno.toIntOrNull() ?: 2026

        _estado.value = estadoOld.copy(
            mes = strMes,
            ano = strAno
        )
        getListaDatosMes(mes, ano)
    }


    private fun getListaDatosMes(mes: Int, ano: Int){

        viewModelScope.launch {
            val estadoOld = _estado.value ?: CalendarioUiEstado()
            _estado.value = estadoOld.copy(isCarganado = true)
            try {
                val lista = calendarioUseCase.getListaMesUseCase(mes, ano)
                val estadoActualizado = _estado.value ?: CalendarioUiEstado()
                _estado.value = estadoActualizado.copy(
                    isCarganado = false,
                    listaMes = lista,
                    msgError = null
                )
            }
            catch (e: Exception){
                val estadoActualizado = _estado.value ?: CalendarioUiEstado()
                _estado.value = estadoActualizado.copy(
                    isCarganado = false,
                    listaMes = null,
                    msgError = "Se produjo un errror: ${e.message}"
                )
            }
        }
    }

    class Factory(
        private val utils: Utils,
        private val calendarioUseCase: CalendarioUseCase
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarioViewModel::class.java)){
                    @Suppress("UNCHECKED_CAST")
                    return CalendarioViewModel(utils, calendarioUseCase) as T
                }
                throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }

}