package com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import java.time.LocalDate

data class FestivosUiEstado(
    val fecha: LocalDate? = null,
    val strTipo: String = "",
    val isSpTipoActivo: Boolean = false,
    val isBtnGuardarActivo: Boolean = false,
    val msgError: String? = null
    )

class FestivosViewModel (
    private val festivosUseCase: FestivosUseCase ,
) : ViewModel() {

    private val _estado = MutableLiveData<FestivosUiEstado>(FestivosUiEstado())
    val estado: LiveData<FestivosUiEstado?> get() = _estado

    //####################################################################
    // Funciones
    //################################################################3333

    fun onItemPulsado(festivo: DatosFestivos){

    }

    fun onItemDeletePulsado(festivo: DatosFestivos){

    }

    fun tvFechaClick(ano: Int, mes: Int, dia: Int) {
        if(ano < 0)return
        var isSPAndBtnActivo = false

        val fecha = LocalDate.of(ano, mes, dia)
        if(fecha != null){
            isSPAndBtnActivo = true
        }
        val estadoOld = _estado.value ?: FestivosUiEstado()
        _estado.value = estadoOld.copy(
            fecha = fecha,
            isSpTipoActivo = isSPAndBtnActivo,
            isBtnGuardarActivo = isSPAndBtnActivo
        )

    }

    fun spFestivosClick(tipoFestivo: String){
        val estadoOld = _estado.value ?: FestivosUiEstado()
        var isBtnActivo = false

        if(estadoOld.fecha != null)isBtnActivo = true

        _estado.value = estadoOld.copy(
            strTipo = tipoFestivo,
            isBtnGuardarActivo = isBtnActivo
        )
    }

    fun clearError() {
        val estadoOld = _estado.value ?: return
        if(estadoOld.msgError != null){
            _estado.value = estadoOld.copy(msgError = null)
        }
    }

    // 🛠️ AGREGA ESTE BLOQUE AL FINAL DEL ARCHIVO (DENTRO DE LA CLASE)
    class Factory(
        private val useCase: FestivosUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FestivosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FestivosViewModel(useCase) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }

}