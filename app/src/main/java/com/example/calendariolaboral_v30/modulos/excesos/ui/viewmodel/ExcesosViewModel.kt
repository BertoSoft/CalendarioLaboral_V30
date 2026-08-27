package com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel

import androidx.lifecycle.ViewModel

data class ExcesosUiEstado(
    val sabados: Int = 0,
    val domingos: Int = 0,
    val nacionales: Int = 0,
    val autonomicos: Int = 0,
    val locales: Int = 0,
    val convenio: Int = 0,
    val msgError: String? = null
        )

class ExcesosViewModel(): ViewModel() {




}