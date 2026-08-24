package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui

import android.R
import android.app.Application
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.databinding.ActivityVacDetalleBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel.VacacionesDetalleViewModel
import java.time.LocalDate

class VacacionesDetalle : AppCompatActivity() {

    private lateinit var binding: ActivityVacDetalleBinding
    private val viewModel: VacacionesDetalleViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        VacacionesDetalleViewModel.Factory(
            vacacionesDetalleUseCase = app.appContainer.vacacionesDetalleUseCase,
            utils = app.appContainer.utils
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
        initVacacionesPendientes()
        //initRv()
        //initVacacionesDisfrutadas()
        //initListeners()

    }

    private fun initListeners() {
        TODO("Not yet implemented")
    }

    private fun initVacacionesDisfrutadas() {
        TODO("Not yet implemented")
    }

    private fun initRv() {
        TODO("Not yet implemented")
    }

    private fun initVacacionesPendientes() {
        val dias = viewModel.getVacacionesPendientes(binding.spAnioDetalle.selectedItem.toString())
        binding.tvDiasPendientesCabecera.text = "${dias.toString()} días."
    }

    private fun initSp() {
        val ano = LocalDate.now().year
        val listaAnos = (ano +1).downTo(2022).map { it.toString() }
        val miAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            listaAnos
        )
        miAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding){
            spAnioDetalle.adapter = miAdapter
            if(spAnioDetalle.selectedItemPosition != 1){
                spAnioDetalle.setSelection(1)
            }
        }
    }

}