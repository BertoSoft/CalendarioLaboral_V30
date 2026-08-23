package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.domain.usecase.VacacionesDetalleUseCase

class VacacionesDetalleViewModel(
    private val vacacionesUseCase: VacacionesDetalleUseCase,
    private val utils: Utils
) {



    class Factory(
        private val vacacionesUseCase: VacacionesDetalleUseCase,
        private val utils: Utils
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VacacionesDetalleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VacacionesDetalleViewModel(vacacionesUseCase, utils) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}