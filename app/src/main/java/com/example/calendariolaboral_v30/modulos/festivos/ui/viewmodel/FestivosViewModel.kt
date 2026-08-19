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

data class DatosFestivoPulsado(
    val festivo: DatosFestivos,
    val isDelete: Boolean = false
    )

class FestivosViewModel (
    private val festivosUseCase: FestivosUseCase ,
    private val utils: Utils
) : ViewModel() {

    private val _listaFestivos = MutableLiveData<List<DatosFestivos>>()
    val listaFestivos: LiveData<List<DatosFestivos>> get() = _listaFestivos

    private val _msgError = MutableLiveData<String?>()
    val msgError: LiveData<String?> get() = _msgError

    private val _isCargando = MutableLiveData<Boolean>()
    val isCargando: LiveData<Boolean> get() = _isCargando

    private val _isEdicionEstadoUi = MutableLiveData<Boolean>()
    val isEdicionEstadoUi: LiveData<Boolean> get() = _isEdicionEstadoUi

    private val _itemPulsadoEstadoUi = MutableLiveData<DatosFestivoPulsado?>()
    val itemPulsadoEstadoUi: LiveData<DatosFestivoPulsado?> get() = _itemPulsadoEstadoUi

    private val _itemDeletePulsadoEstadoUi = MutableLiveData<DatosFestivoPulsado?>()
    val itemDeletePulsadoEstadoUi: LiveData<DatosFestivoPulsado?> get() = _itemDeletePulsadoEstadoUi

    //####################################################################
    // Funciones
    //################################################################3333
    fun getAllFestivos(strAno: String){
        _isCargando.value = true
        _itemPulsadoEstadoUi.value = null
        _itemDeletePulsadoEstadoUi.value = null

        viewModelScope.launch {
            try {
                val lista = festivosUseCase.getAllFestivosUseCase(strAno)
                if(lista.isEmpty()){
                    _listaFestivos.value = lista
                }
                else {
                    _listaFestivos.value = lista
                }
            }
            catch (e: Exception){
                _msgError.value = "Error al cargar los datos: ${e.localizedMessage}"
            }
            finally {
                _isCargando.value = false
            }
        }
    }

    fun setFestivo(festivo: DatosFestivos){
        _isCargando.value = true
        viewModelScope.launch {
            try {
                val todoOk = festivosUseCase.setFestivoUseCase(festivo)
                if(todoOk){
                    getAllFestivos(festivo.fecha.year.toString())
                    setModoEdicion(false)
                }
            }
            catch (e: Exception){
                _msgError.value = "Error al guardar: ${e.localizedMessage}"
            }
            finally {
                _isCargando.value = false
            }
        }
    }

    fun setModoEdicion(isEdicion: Boolean){
        _isEdicionEstadoUi.value = isEdicion
    }

    fun onItemPulsado(festivo: DatosFestivos){
        setModoEdicion(true)
        _itemPulsadoEstadoUi.value = DatosFestivoPulsado(festivo = festivo)
    }

    fun onItemDeletePulsado(festivo: DatosFestivos){
        _isCargando.value = true
        viewModelScope.launch {
            try {
                val todoOk = festivosUseCase.delFestivoUseCase(festivo)
                if(todoOk){
                    _itemPulsadoEstadoUi.value = null
                    _isEdicionEstadoUi.value = false
                    getAllFestivos(festivo.fecha.year.toString())
                    setModoEdicion(false)

                    // Se emite el evento solo tras confirmar el éxito real en el hilo/proceso
                    _itemDeletePulsadoEstadoUi.value = DatosFestivoPulsado(festivo = festivo, isDelete = true)
                } else {
                    _msgError.value = "No se pudo eliminar el día festivo."
                }
            }
            catch (e: Exception){
                _msgError.value = "No se pudo eliminar el registro"
            }
            finally {
                _isCargando.value = false
            }
        }
    }

    fun clearDeleteObsever() {
        _itemDeletePulsadoEstadoUi.value = null
    }

    fun clearErrorObserver() {
        _msgError.value = null
    }
    // ... Todo tu código interno actual se queda exactamente igual ...

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