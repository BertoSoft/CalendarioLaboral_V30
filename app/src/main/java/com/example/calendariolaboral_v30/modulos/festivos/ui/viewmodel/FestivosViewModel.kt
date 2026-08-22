package com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toTipoFestivo
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FestivosUiEstado(
    val fecha: LocalDate? = null,
    val strTipo: String = "",
    val isSpTipoActivo: Boolean = false,
    val isBtnGuardarActivo: Boolean = false,
    val msgError: String? = null,
    val listaFestivos: List<DatosFestivos> = emptyList(),
    val isCargando: Boolean = false
    )

class FestivosViewModel (
    private val festivosUseCase: FestivosUseCase ,
) : ViewModel() {

    private val _estado = MutableLiveData<FestivosUiEstado>(FestivosUiEstado())
    val estado: LiveData<FestivosUiEstado> get() = _estado

    //####################################################################
    // Funciones
    //################################################################3333

    fun itemClick(festivo: DatosFestivos){
        _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
            fecha = festivo.fecha,
            strTipo = festivo.tipo.name,
            isSpTipoActivo = true,
            isBtnGuardarActivo = true
        )
    }

    fun itemDeleteClick(festivo: DatosFestivos){
        _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
            isCargando = true,
        )
        viewModelScope.launch {
            try {
                if(festivosUseCase.delFestivoUseCase(festivo)){
                    val strAno = festivo.fecha.year.toString()
                        val lista = festivosUseCase.getAllFestivosUseCase(strAno)
                    _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                        listaFestivos = lista,
                        msgError = null,
                        isCargando = false,
                        isSpTipoActivo = false,
                        isBtnGuardarActivo = false
                    )
                }
            }
            catch (e: Exception){
                _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                    listaFestivos = emptyList(), // O null, según cómo lo tengas definido
                    msgError = "Error al cargar los datos: ${e.message}",
                    isCargando = false,
                    isSpTipoActivo = false,
                    isBtnGuardarActivo = false
                )
            }
        }

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

    fun btnGuardarClick() {

        _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
           isCargando = true
        )

        viewModelScope.launch {
            try {
                val estadoOld = _estado.value ?: return@launch
                val fecha = estadoOld.fecha ?: return@launch

                val tipo = TipoFestivos.entries.find { it.name == estadoOld.strTipo }
                    ?: estadoOld.strTipo.toTipoFestivo()

                val id = festivosUseCase.existeFestivoUseCase(DatosFestivos(-1, fecha, tipo))
                val dato = DatosFestivos(id, fecha, tipo)
                if(festivosUseCase.setFestivoUseCase(dato)){
                    val strAno = fecha.year.toString()
                    val lista = festivosUseCase.getAllFestivosUseCase(strAno)
                    _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                        listaFestivos = lista,
                        msgError =  null,
                        isCargando = false,
                        isSpTipoActivo = false,
                        isBtnGuardarActivo = false
                    )
                }
            }
            catch (e: Exception){
                _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                    msgError = "Error al guardar los datos: ${e.message}",
                    isCargando = false,
                    isSpTipoActivo = false,
                    isBtnGuardarActivo = false
                )
            }
        }
    }

    fun spAnoClick(strAno: String) {
        if(strAno.isBlank())return
        _estado.value = (_estado.value ?: FestivosUiEstado()).copy(isCargando = true)
        viewModelScope.launch {
            try {
                val listaFestivos = festivosUseCase.getAllFestivosUseCase(strAno)
                _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                    listaFestivos = listaFestivos,
                    msgError = if(listaFestivos.isEmpty()) "Lista Vacía..." else null,
                    isCargando = false,
                    isSpTipoActivo = false,
                    isBtnGuardarActivo = false
                )
            }
            catch (e: Exception){
                _estado.value = (_estado.value ?: FestivosUiEstado()).copy(
                    listaFestivos = emptyList(), // O null, según cómo lo tengas definido
                    msgError = "Error al cargar los datos: ${e.message}",
                    isCargando = false,
                    isSpTipoActivo = false,
                    isBtnGuardarActivo = false
                )
            }
        }
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