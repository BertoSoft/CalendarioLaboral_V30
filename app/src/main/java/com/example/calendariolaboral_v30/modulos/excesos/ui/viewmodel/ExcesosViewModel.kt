package com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.modulos.excesos.domain.usecase.ExcesosUseCase
import kotlinx.coroutines.launch

data class ExcesosUiEstado(
    val sabados: Int = 0,
    val domingos: Int = 0,
    val nacionales: Int = 0,
    val autonomicos: Int = 0,
    val locales: Int = 0,
    val convenio: Int = 0,
    val isCargando: Boolean = false,
    val msgError: String? = null
        )

class ExcesosViewModel(
    private val excesosUseCase: ExcesosUseCase
): ViewModel() {

    val _estado = MutableLiveData<ExcesosUiEstado>(ExcesosUiEstado())
    val estado: LiveData<ExcesosUiEstado> get() = _estado

    fun spAnoClick(strAno: String) {
        val estadoOld = _estado.value ?: ExcesosUiEstado()
        _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                val datos = excesosUseCase.getDatosUseCase(strAno.toInt())
                _estado.value = estadoOld.copy(
                    sabados = datos.sabados,
                    domingos = datos.domingos,
                    nacionales = datos.nacionales,
                    autonomicos = datos.autonomicos,
                    locales = datos.locales,
                    convenio = datos.convenio,
                    isCargando = false,
                    msgError = null
                )

            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    isCargando = false,
                    msgError = "Error al obtener los datos..."
                )
            }
        }
    }

    class Factory(
        private val excesosUseCase: ExcesosUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExcesosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExcesosViewModel( excesosUseCase) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }


}