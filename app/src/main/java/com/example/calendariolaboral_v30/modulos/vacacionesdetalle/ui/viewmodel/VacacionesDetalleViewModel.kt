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
    val lista_vacas_pendientes: List<DatosVacasPendientes> = emptyList()
)

class VacacionesDetalleViewModel(
    private val vacacionesDetalleUseCase: VacacionesDetalleUseCase,
    private val utils: Utils,
    private val vacacionesUseCase: VacacionesUseCase
): ViewModel() {

    private val _estado = MutableLiveData<VacasPendientesUiEstado>(VacasPendientesUiEstado())
    val estado: LiveData<VacasPendientesUiEstado> get() = _estado

    fun getDiasVacasPendientes(){
        val estadoOld = _estado.value ?: VacasPendientesUiEstado()
        viewModelScope.launch {
            try {
                _estado.value = estadoOld.copy(isCargando = true)
                var lista = vacacionesDetalleUseCase.getDiasVacasPendientesUseCasse()
                if(lista.isEmpty()){
                    // Creamos la lista inicial en lista_tmp
                    val ano_actual = LocalDate.now().year
                    var ano = 2021
                    var dias = 9
                    val listatmp = mutableListOf<DatosVacasPendientes>()
                    while (ano <= ano_actual + 1){
                        listatmp.add(DatosVacasPendientes(
                            -1,
                            ano.toString(),
                            dias
                        ))
                        ano ++
                        dias += 22
                    }
                    if(vacacionesDetalleUseCase.initVacasPendientesUseCase(listatmp)){
                        lista = vacacionesDetalleUseCase.getDiasVacasPendientesUseCasse()
                    }
                }
                _estado.value = estadoOld.copy(
                    msg_error = null,
                    lista_vacas_pendientes = lista,
                    isCargando = false
                )
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    msg_error = "Se produjo un errror: ${e.message}",
                    lista_vacas_pendientes = emptyList(),
                    isCargando = false
                )
            }
        }
    }

    fun getAllVacaciones(strAno: String) {
        val estadoOld = _estado.value ?: VacasPendientesUiEstado()
        viewModelScope.launch {
            try {
                _estado.value = estadoOld.copy(isCargando = true)
                val lista = vacacionesUseCase.getAllVacacionesUseCase(strAno)
                _estado.value = estadoOld.copy(
                    lista_vacaciones = lista,
                    isCargando = false,
                    msg_error = null
                )
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    lista_vacaciones = emptyList(),
                    isCargando = false,
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
                return VacacionesDetalleViewModel(vacacionesDetalleUseCase, utils, vacacionesUseCase) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}