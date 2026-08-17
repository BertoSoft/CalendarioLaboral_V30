package com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    private val _isEdicion = MutableLiveData<Boolean>()
    val isEdicion: LiveData<Boolean> get() = _isEdicion

    private val _itemFestivoPulsado = MutableLiveData<DatosFestivos>()
    val itemFestivoPulsado: LiveData<DatosFestivos> get() = _itemFestivoPulsado


    fun setModoEdicion(isEdicion: Boolean){
        _isEdicion.value = isEdicion
    }

    fun itemFestivoPulsado(festivo: DatosFestivos) {
        _itemFestivoPulsado.value = festivo
    }

    //####################################################################
    // Funciones
    //################################################################3333
    fun getAllFestivos(strAno: String){
        _isCargando.value = true
        viewModelScope.launch {
            try {
                val lista = festivosUseCase.getAllFestivosUseCase(strAno)
                if(lista.isEmpty()){
                    _msgError.value = "No existen festivos para $strAno"
                }
                else {
                    _listaFestivos.value = lista
                    _msgError.value = null
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

    fun setFestivo(fecha: LocalDate, tipoFestivo: TipoFestivo){
        _isCargando.value = true
        viewModelScope.launch {
            try {
                val dato = DatosFestivos(
                    -1,
                    fecha,
                    tipoFestivo
                )
                val todoOk = festivosUseCase.setFestivoUseCase(dato)
                if(todoOk){
                    getAllFestivos(fecha.year.toString())
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