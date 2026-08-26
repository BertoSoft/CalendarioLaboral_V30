package com.example.calendariolaboral_v30.modulos.backup.ui.viewmodel

import android.R
import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendariolaboral_v30.modulos.backup.domain.usecase.BackupUseCase
import kotlinx.coroutines.launch

data class BackupUiEstado(
    val isExportar: Boolean = false,
    val isImportar: Boolean = false,
    val isCargando: Boolean = false,
    val msgError: String? = null
)

class BackupViewModel(
    private val backupUseCase: BackupUseCase
): ViewModel() {

    private val _estado = MutableLiveData<BackupUiEstado>(BackupUiEstado())
    val estado: LiveData<BackupUiEstado> get() = _estado

    fun setExportar(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isExportar = true)
    }

    fun setImportar(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isImportar =  true)
    }

    fun clearExportar(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isExportar = false)
    }

    fun clearImportar(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isImportar = false)
    }

    fun clearError(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(msgError = null)
    }

    // Logica de copia con subrrutinas
    fun guardarCopia(uri: Uri){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                if(backupUseCase.guardarCopia(uri)){
                    _estado.value = estadoOld.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "Copia guardada con exito..."
                    )
                }
                else{
                    _estado.value = estadoOld.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "No se pudo guardar la copia..."
                    )
                }
            }
            catch (e: Exception){
                _estado.value = estadoOld.copy(
                    isCargando = false,
                    isExportar = false,
                    isImportar = false,
                    msgError = "Se produjo un errror ${e.message}"
                )
            }
        }
    }

    fun abrirCopia(uri: Uri){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isCargando = true)
        viewModelScope.launch {
            try {
                if(backupUseCase.abrirCopia(uri)){
                    _estado.postValue( estadoOld.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "Copia de datos cargada con exito..."
                    ))
                }
                else{
                    _estado.postValue(estadoOld.copy(
                        isCargando = false,
                        isExportar = false,
                        isImportar = false,
                        msgError = "No se pudo cargar la copia..."
                    ))
                }
            }
            catch (e: Exception){
                _estado.postValue(estadoOld.copy(
                    isCargando = false,
                    isExportar = false,
                    isImportar = false,
                    msgError = "Se produjo un errror ${e.message}"
                ))
            }
        }
    }

    class Factory(
        private val backupUseCase: BackupUseCase
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BackupViewModel(backupUseCase) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }


}