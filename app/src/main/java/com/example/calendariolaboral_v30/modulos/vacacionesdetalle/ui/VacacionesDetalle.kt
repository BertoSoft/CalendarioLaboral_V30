package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.databinding.ActivityVacDetalleBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel.VacacionesDetalleViewModel

class VacacionesDetalle : AppCompatActivity() {

    private lateinit var binding: ActivityVacDetalleBinding
    private val viewModel: VacacionesDetalleViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        VacacionesDetalleViewModel.Factory(
            vacacionesUseCase = app.appContainer.vacacionesUseCase,
            utils = app.appContainer.utils
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}