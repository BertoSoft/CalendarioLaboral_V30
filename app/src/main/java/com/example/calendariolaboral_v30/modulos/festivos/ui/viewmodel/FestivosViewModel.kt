package com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel

import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.domain.usecase.FestivosUseCase
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toTipoFestivo
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FestivoPulsadoEstadoUi(
    val strFechaLarga: String,
    val indice: Int,
    val isFestivoPulsado: Boolean
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

    private val _itemFestivoPulsadoEstadoUi = MutableLiveData<FestivoPulsadoEstadoUi?>()
    val itemFestivoPulsadoEstadoUi: LiveData<FestivoPulsadoEstadoUi?> get() = _itemFestivoPulsadoEstadoUi

    //####################################################################
    // Funciones
    //################################################################3333
    fun getAllFestivos(strAno: String){
        _isCargando.value = true
        _itemFestivoPulsadoEstadoUi.value = null

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

    fun setFestivo(strFecha: String, strTipo: String){
        _isCargando.value = true

        viewModelScope.launch {
            try {
                val fecha = utils.fromFechaLargaToLocalDate(strFecha)
                val tipo = strTipo.toTipoFestivo()
                val dato = DatosFestivos(
                    -1,
                    fecha,
                    tipo
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

    fun delFestivo(strFecha: String, strTipo: String){
        _isCargando.value = true

        viewModelScope.launch {
            val fecha = utils.fromFechaLargaToLocalDate(strFecha)
            val tipo = strTipo.toTipoFestivo()
            val id = festivosUseCase.existeFestivoUseCase(DatosFestivos(-1,fecha,tipo))
            val dato = DatosFestivos(id, fecha, tipo)

            try {
                val todoOk = festivosUseCase.delFestivoUseCase(dato)
                if(todoOk){
                    _itemFestivoPulsadoEstadoUi.value = null
                    _isEdicionEstadoUi.value = false
                    val anoActual = fecha.year.toString()
                    getAllFestivos(anoActual)
                }
                else{
                    _msgError.value = "No se pudo eliminar el día festivo."
                }
            }
            catch (e: Exception){
                _msgError.value = "Error al eliminar: ${e.localizedMessage}"
            }
            finally {
                _isCargando.value = false
            }
        }
        _isCargando.value = false
    }

    fun setFechaSelecionada(dia: Int, mes: Int, ano: Int) {
        val fecha = LocalDate.of(ano, mes, dia)
        val strFecha = utils.fromLocalDateToFechaLarga(fecha)
        val posicion = _itemFestivoPulsadoEstadoUi.value?.indice ?: 0

        _itemFestivoPulsadoEstadoUi.value = FestivoPulsadoEstadoUi(
            strFecha,
            posicion,
            isFestivoPulsado = false
        )
    }

    fun setModoEdicion(isEdicion: Boolean){
        _isEdicionEstadoUi.value = isEdicion
    }

    fun itemFestivoPulsado(festivo: DatosFestivos) {
        val posicion = TipoFestivo.entries.indexOf(festivo.tipo)
        val strFechaLarga = utils.fromLocalDateToFechaLarga(festivo.fecha)

        _itemFestivoPulsadoEstadoUi.value = FestivoPulsadoEstadoUi(
            strFechaLarga = strFechaLarga,
            indice = if(posicion != -1) posicion else 0,
            isFestivoPulsado =true
        )
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