package com.example.calendariolaboral_v30.modulos.backup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

data class BackupUiEstado(
    val isExportar: Boolean = false,
    val isImportar: Boolean = false,
    val msgError: String? = null
)

class BackupViewModel(
    private val aplicacion: Application
): ViewModel() {

    private val _estado = MutableLiveData<BackupUiEstado>(BackupUiEstado())
    val estado: LiveData<BackupUiEstado> get() = _estado

    fun getExportar(){
        val estadoOld = _estado.value ?: BackupUiEstado()
        _estado.value = estadoOld.copy(isExportar = true)
    }

    fun getImportar(){
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

    class Factory(
        private val aplicacion: Application
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BackupViewModel(aplicacion) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }


}