package com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toTipoFestivo
import kotlinx.coroutines.launch

data class FestivosUiEstado(
    val strFecha: String = "",
    val strTipo: String = "",
    val isSpTipoActivo: Boolean = false,
    val isBtnGuardarActivo: Boolean = false,
    val msgError: String? = null
    )

class FestivosViewModel (
    private val festivosUseCase: FestivosUseCase ,
    private val utils: Utils
) : ViewModel() {

    private val _estado = MutableLiveData<FestivosUiEstado>()
    val estado: LiveData<FestivosUiEstado?> get() = _estado

    //####################################################################
    // Funciones
    //################################################################3333

    fun clearError() {
        val estadoOld = _estado.value ?: return
        if(estadoOld.msgError != null){
            _estado.value = estadoOld.copy(msgError = null)
        }
    }

    fun onFechaSeleccionada(ano: Int, mes: Int, dia: Int) {
        TODO("Not yet implemented")
    }

    // 🛠️ AGREGA ESTE BLOQUE AL FINAL DEL ARCHIVO (DENTRO DE LA CLASE)
    class Factory(
        private val useCase: FestivosUseCase,
        private val utils: Utils
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FestivosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FestivosViewModel(useCase, utils) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }

}