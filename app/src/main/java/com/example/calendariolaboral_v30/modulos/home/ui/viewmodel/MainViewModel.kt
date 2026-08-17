package com.example.calendariolaboral_v30.modulos.home.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendariolaboral_v30.modulos.home.domain.model.DatosMenu

class MainViewModel : ViewModel() {

    //#############################################################
    // Variables LiveData
    //##############################################################
    private val _navegarAModulo = MutableLiveData<DatosMenu?>()
    val navegarAModulo: LiveData<DatosMenu?> get() = _navegarAModulo

    private val _eventoSalir = MutableLiveData<Boolean>(false)
    val eventoSalir: LiveData<Boolean> get() = _eventoSalir

    //###################################################################
    // Funciones ViewModel
    //####################################################################
    fun tarjetaPulsada(menu: DatosMenu) {
        _navegarAModulo.value = menu
    }

    /**
     * Función crucial en MVVM: se llama desde la Activity inmediatamente después
     * de cambiar de pantalla. Limpia el estado para evitar que, si el usuario regresa
     * atrás, la app vuelva a saltar de pantalla en bucle.
     */
    fun navegacionCompletada() {
        _navegarAModulo.value = null
    }

    fun ejecutarSalir() {
        _eventoSalir.value = true
    }

}