package com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class VacacionesUiState(
    val strFechaInicio: String = "",
    val strFechaFinal: String = "",
    val isFechaFinHabilitada: Boolean = false,
    val isBtnGuardarHabilitado: Boolean = false,
    val isMostrarCalendario: Boolean = false,
    val msgError: String? = null
)

class VacacionesViewModel(
    private val vacacionesUsecase: VacacionesUseCase,
    private val utils: Utils
): ViewModel() {

    private val _estado = MutableLiveData<VacacionesUiState>(VacacionesUiState())
    val estado: LiveData<VacacionesUiState> get() = _estado

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    //######################################################################3
    // Funciones
    //######################################################################3
    fun onFechaInicioSeleccionada(ano: Int, mes: Int, dia: Int){
        if(ano < 0) return

        val fecha: LocalDate? = LocalDate.of(ano, mes + 1, dia)
        var strFechaInicio = ""
        if(fecha != null)strFechaInicio = utils.fromLocalDateToFechaCorta(fecha)
        val estadoOld = _estado.value ?: VacacionesUiState()

        if(strFechaInicio.isBlank()){
            _estado.value = estadoOld.copy(
                isFechaFinHabilitada = false,
                isBtnGuardarHabilitado = false,
                isMostrarCalendario = false,
                msgError = "Debes de seleccionar una fecha de inicio..."
            )
        }
        else if(estadoOld.strFechaFinal.isBlank()){

            _estado.value = estadoOld.copy(
                strFechaInicio = strFechaInicio,
                isFechaFinHabilitada = true,
                isMostrarCalendario = true,
                msgError = null
            )
        }
        else{
            _estado.value = estadoOld.copy(
                strFechaInicio = strFechaInicio,
                isFechaFinHabilitada = true,
                isMostrarCalendario = false,
                msgError = null
            )
        }
    }

    fun onFechaFinalSeleccionada(ano: Int, mes: Int, dia: Int){
        val estadoOld = _estado.value ?: VacacionesUiState()

        // ⚡ CLÁUSULA DE ESCAPE ANTI-DUPLICADOS:
        // Si la vista ya apagó el calendario, ignoramos llamadas fantasma del sistema
        //if (!estadoOld.isMostrarCalendario && estadoOld.strFechaFinal.isNotBlank()) {
         //   return
       // }


        if(ano < 0){
            _estado.value = estadoOld.copy(isMostrarCalendario = false)
            return
        }
        val fecha: LocalDate? = LocalDate.of(ano, mes + 1, dia)
        var strFechaFinal = ""
        if(fecha != null)strFechaFinal = utils.fromLocalDateToFechaCorta(fecha)

        if(strFechaFinal.isBlank()){
            _estado.value = estadoOld.copy(
                isBtnGuardarHabilitado = false,
                isMostrarCalendario = false,
                msgError = "Debes de seleccionar una fecha final..."
            )
        }
        else{
            viewModelScope.launch {
                val todoOk = vacacionesUsecase.isFechasValidas(DatosVacaciones(
                    -1,
                    estadoOld.strFechaInicio,
                    strFechaFinal
                ))
                if(todoOk){
                    _estado.value = estadoOld.copy(
                        strFechaFinal = strFechaFinal,
                        isBtnGuardarHabilitado = true,
                        isMostrarCalendario = false,
                        msgError = null
                    )
                }
                else{
                    _estado.value = estadoOld.copy(
                        strFechaFinal = "",
                        isBtnGuardarHabilitado = false,
                        isMostrarCalendario = false,
                        msgError = "La fecha final debe ser posterior a la de inicio..."
                    )
                }
            }

        }


    }

    fun onItemPulsado(registro: DatosVacaciones){

    }

    fun clearError(){
        val estadoOld = _estado.value ?: return
        if(estadoOld.msgError != null){
            _estado.value = estadoOld.copy(msgError = null)
        }
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