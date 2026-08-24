package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.model.DatosVacasPendientes
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase.VacacionesDetalleUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

data class VacasPendientesUiEstado(
    val isCargando: Boolean = false,
    val msg_error: String? = null,
    val lista: List<DatosVacasPendientes> = emptyList()
)

class VacacionesDetalleViewModel(
    private val vacacionesDetalleUseCase: VacacionesDetalleUseCase,
    private val utils: Utils
): ViewModel() {

    private val _estado = MutableLiveData<VacasPendientesUiEstado>(VacasPendientesUiEstado())
    val estado: LiveData<VacasPendientesUiEstado> get() = _estado

    fun getVacacionesPendientes(): List<DatosVacasPendientes>{
        val estadoOld = _estado.value ?: VacasPendientesUiEstado()
        viewModelScope.launch {
            try {
                _estado.value = estadoOld.copy(isCargando = true)

                var lista = vacacionesDetalleUseCase.getVacacionesPendientesUseCase()
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
                        lista = vacacionesDetalleUseCase.getVacacionesPendientesUseCase()
                    }
                }

            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    msg_error = "Se produjo un errror: ${e.message}",
                    lista = emptyList()
                )
            }
        }


        return emptyList()
    }



    class Factory(
        private val vacacionesDetalleUseCase: VacacionesDetalleUseCase,
        private val utils: Utils
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(
                    VacacionesDetalleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VacacionesDetalleViewModel(vacacionesDetalleUseCase, utils) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}