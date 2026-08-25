package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase.VacacionesDetalleUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

data class VacasPendientesUiEstado(
    val isCargando: Boolean = false,
    val msg_error: String? = null,
    val lista_vacaciones: List<DatosVacaciones> = emptyList(),
    val vacas_atrasadas: Int = 0,
    val vacas_disfrutadas: Int = 0,
    val vacas_pendientes: Int = 0
)

class VacacionesDetalleViewModel(
    private val vacacionesDetalleUseCase: VacacionesDetalleUseCase,
): ViewModel() {

    private val _estado = MutableLiveData<VacasPendientesUiEstado>(VacasPendientesUiEstado())
    val estado: LiveData<VacasPendientesUiEstado> get() = _estado

    fun getDatos(strAno: String){
        val estadoOld = _estado.value ?: VacasPendientesUiEstado()
        _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                val datos = vacacionesDetalleUseCase.getDatosUseCase(strAno.toInt())
                _estado.value = estadoOld.copy(
                    lista_vacaciones = datos.lista,
                    vacas_atrasadas = datos.dias_atrasados,
                    vacas_disfrutadas = datos.dias_disfrutados,
                    vacas_pendientes = datos.dias_pendientes,
                    msg_error = null
                )
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    lista_vacaciones = emptyList(),
                    vacas_atrasadas = 0,
                    vacas_disfrutadas = 0,
                    vacas_pendientes = 0,
                    msg_error = "Se produjo un error: ${e.message}"
                )
            }
        }

    }

    fun clearError(){
        val estadoOld = _estado.value ?: VacasPendientesUiEstado()
        _estado.value = estadoOld.copy(
            msg_error = null
        )
    }

    class Factory(
        private val vacacionesDetalleUseCase: VacacionesDetalleUseCase,
        private val utils: Utils,
        private val vacacionesUseCase: VacacionesUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(
                    VacacionesDetalleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VacacionesDetalleViewModel(vacacionesDetalleUseCase) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}