package com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import java.time.format.DateTimeFormatter

data class VacacionesUiState(
    val strFechaInicio: String,
    val strFechaFin: String,
    val isFechaFinHabilitada: Boolean,
    val isBtnGuardarHabilitado: Boolean,
    val msgError: String? = null
)

class VacacionesViewModel(
    private val vacacionesUsecase: VacacionesUseCase,
    private val utils: Utils
): ViewModel() {

    private val _estado = MutableLiveData<VacacionesUiState>()
    val estado: LiveData<VacacionesUiState> get() = _estado

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    //######################################################################3
    // Funciones
    //######################################################################3
    fun onFechaInicioSeleccionada(strFechaInicio: String){
        if(strFechaInicio.isBlank()){
            _estado.value = _estado.value?.copy(
                strFechaInicio = "",
                strFechaFin =  "",
                isFechaFinHabilitada = false,
                isBtnGuardarHabilitado = false,
                msgError = "Debes de seleccionar una fecha de inicio..."
            )
        }
        else{
            _estado.value = _estado.value?.copy(
                strFechaInicio = strFechaInicio,
                strFechaFin =  "",
                isFechaFinHabilitada = true,
                isBtnGuardarHabilitado = false,
                msgError = "Debes de seleccionar una fecha de inicio..."
            )
        }
    }

    fun onFechaFinalSeleccionada(strFechaFinal: String){

    }









    // 🛠️ AGREGA ESTE BLOQUE AL FINAL DEL ARCHIVO (DENTRO DE LA CLASE)
    class Factory(
        private val vacacionesUseCase: VacacionesUseCase,
        private val utils: Utils
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VacacionesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VacacionesViewModel(vacacionesUseCase, utils) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}