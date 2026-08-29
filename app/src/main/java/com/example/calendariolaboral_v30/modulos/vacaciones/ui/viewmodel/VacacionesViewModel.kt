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
    val fecha_inicio: LocalDate? = null,
    val fecha_final: LocalDate? = null,
    val lista: List<DatosVacaciones> = emptyList(),
    val isFechaFinHabilitada: Boolean = false,
    val isBtnGuardarHabilitado: Boolean = false,
    val isMostrarCalendario: Boolean = false,
    val isCargando: Boolean = true,
    val msgError: String? = null
)

class VacacionesViewModel(
    private val vacacionesUsecase: VacacionesUseCase,
    private val utils: Utils,
): ViewModel() {

    private val _estado = MutableLiveData<VacacionesUiState>(VacacionesUiState())
    val estado: LiveData<VacacionesUiState> get() = _estado

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    //######################################################################3
    // Funciones
    //######################################################################3
    fun tvFechaInicialClick(ano: Int, mes: Int, dia: Int){
        if(ano < 0) return

        val fecha: LocalDate? = LocalDate.of(ano, mes, dia)
        val estadoOld = _estado.value ?: VacacionesUiState()

        if(fecha == null){
            _estado.value = estadoOld.copy(
                fecha_inicio = null,
                isFechaFinHabilitada = false,
                isBtnGuardarHabilitado = false,
                isMostrarCalendario = false,
                msgError = "Debes de seleccionar una fecha de inicio..."
            )
        }
        else if(estadoOld.fecha_final == null){

            _estado.value = estadoOld.copy(
                fecha_inicio = fecha,
                isFechaFinHabilitada = true,
                isMostrarCalendario = true,
                msgError = null
            )
        }
        else{
            _estado.value = estadoOld.copy(
                isCargando = true
            )
            viewModelScope.launch {
                //Las dos estan cubiertas
                val todoOk = vacacionesUsecase.isFechasValidas(DatosVacaciones(
                    -1,
                    fecha,
                    estadoOld.fecha_final,
                    -1
                ))
                if(todoOk){
                    _estado.value = estadoOld.copy(
                        fecha_inicio = fecha,
                        isFechaFinHabilitada = true,
                        isMostrarCalendario = false,
                        isCargando = false,
                        msgError = null
                    )
                }
                else{
                    _estado.value = estadoOld.copy(
                        fecha_inicio = null,
                        isFechaFinHabilitada = true,
                        isMostrarCalendario = false,
                        isCargando = false,
                        msgError = "La fecha inicial debe ser anterior a la final..."
                    )
                }
            }
        }
    }

    fun tvFechaFinalClick(ano: Int, mes: Int, dia: Int){
        val estadoOld = _estado.value ?: VacacionesUiState()

        if(ano < 0){
            _estado.value = estadoOld.copy(isMostrarCalendario = false)
            return
        }
        val fecha: LocalDate? = LocalDate.of(ano, mes, dia)

        if(fecha == null){
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
                    estadoOld.fecha_inicio!!,
                    fecha,
                    -1
                ))
                if(todoOk){
                    _estado.value = estadoOld.copy(
                        fecha_final = fecha,
                        isBtnGuardarHabilitado = true,
                        isMostrarCalendario = false,
                        msgError = null
                    )
                }
                else{
                    _estado.value = estadoOld.copy(
                        fecha_final = null,
                        isBtnGuardarHabilitado = false,
                        isMostrarCalendario = false,
                        msgError = "La fecha final debe ser posterior a la de inicio..."
                    )
                }
            }
        }
    }

    fun btnGuardarClick(){
        val estadoOld = _estado.value ?: VacacionesUiState()
        _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                val estadoOld = _estado.value ?: return@launch
                val fecha_inicio = _estado.value?.fecha_inicio ?: return@launch
                val fecha_final = _estado.value?.fecha_final ?: return@launch
                val id = vacacionesUsecase.existeVacacionesUseCase(DatosVacaciones(
                    -1,
                    fecha_inicio,
                    fecha_final,
                    -1
                ))
                val dato = DatosVacaciones(
                    id,
                    fecha_inicio,
                    fecha_final,
                    -1
                )
                if(vacacionesUsecase.setVacacionesUseCase(dato)){
                    val strAno = dato.fecha_inicio.year.toString()
                    val lista = vacacionesUsecase.getAllVacacionesUseCase(strAno)
                    _estado.value = estadoOld.copy(
                        fecha_inicio = null,
                        fecha_final = null,
                        lista = lista,
                        msgError = null,
                        isCargando = false,
                        isBtnGuardarHabilitado = false,
                        isMostrarCalendario = false,
                        isFechaFinHabilitada = false
                    )
                }
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    msgError = "se produjo un errror: ${e.message}",
                    isBtnGuardarHabilitado = false,
                    isCargando = false,
                    isMostrarCalendario = false,
                    isFechaFinHabilitada = false
                )
            }
        }
    }

    fun spAnoClick(strAno: String){
        val estadoOld =  _estado.value ?: VacacionesUiState()
            _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                val lista = vacacionesUsecase.getAllVacacionesUseCase(strAno) ?: emptyList()
                _estado.value = estadoOld.copy(
                    lista = lista,
                    isCargando = false,
                    msgError = if(lista.isEmpty())" Lista de Vacaciones Vacía" else null
                )
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    lista = emptyList(),
                    isCargando = false,
                    msgError = "Se produjo un errro al cargar la lista: ${e.message}"
                )
            }
        }
    }

    fun itemClick(vacaciones: DatosVacaciones){
        val estadoOld = _estado.value ?: VacacionesUiState()
        _estado.value = estadoOld.copy(
            fecha_inicio = vacaciones.fecha_inicio,
            fecha_final = vacaciones.fecha_final,
            isMostrarCalendario = false,
            isFechaFinHabilitada = true,
            isBtnGuardarHabilitado = true,
        )
    }

    fun itemDeleteClick(vacaciones: DatosVacaciones){
        val estadoOld = _estado.value ?: VacacionesUiState()
        _estado.value = estadoOld.copy(isCargando = true)

        viewModelScope.launch {
            try {
                if (vacacionesUsecase.delVacaciones(vacaciones)){
                    val str_ano = vacaciones.fecha_inicio.year.toString()
                    val lista = vacacionesUsecase.getAllVacacionesUseCase(str_ano)
                    _estado.value = estadoOld.copy(
                        fecha_inicio = null,
                        fecha_final = null,
                        lista = lista,
                        isCargando = false,
                        isMostrarCalendario = false,
                        isFechaFinHabilitada = false,
                        isBtnGuardarHabilitado = false,
                        msgError = null
                    )
                }
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    fecha_inicio = null,
                    fecha_final = null,
                    lista = emptyList(),
                    isCargando = false,
                    isMostrarCalendario = false,
                    isFechaFinHabilitada = false,
                    isBtnGuardarHabilitado = false,
                    msgError = "Se produjo un error: ${e.message} "
                )
            }
        }
    }

    fun clearError(){
        val estadoOld = _estado.value ?: return
        if(estadoOld.msgError != null){
            _estado.value = estadoOld.copy(msgError = null)
        }
    }

    fun clearMostrarCalendario(){
        val estadoOld = _estado.value ?: return
        if(estadoOld.isMostrarCalendario){
            _estado.value = estadoOld.copy(isMostrarCalendario = false)
        }
    }

    class Factory(
        private val vacacionesUseCase: VacacionesUseCase,
        private val utils: Utils,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VacacionesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VacacionesViewModel( vacacionesUseCase, utils) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}