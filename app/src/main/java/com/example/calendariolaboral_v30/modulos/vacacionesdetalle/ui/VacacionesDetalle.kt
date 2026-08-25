package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui

import android.R
import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.databinding.ActivityVacDetalleBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.usecase.VacacionesUseCase
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.adapter.VacacionesAdapter
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.adapter.VacasPendientesAdapter
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.extensions.toDias
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel.VacacionesDetalleViewModel
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.viewmodel.VacasPendientesUiEstado
import java.time.LocalDate

class VacacionesDetalle : AppCompatActivity() {

    private lateinit var binding: ActivityVacDetalleBinding
    private val miAdapter = VacasPendientesAdapter()
    private val viewModel: VacacionesDetalleViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        VacacionesDetalleViewModel.Factory(
            vacacionesDetalleUseCase = app.appContainer.vacacionesDetalleUseCase,
            utils = app.appContainer.utils,
            vacacionesUseCase = app.appContainer.vacacionesUseCase
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
        initRv()
        initDatos()
        initListeners()
        initObserves()

    }

    private fun initDatos() {
        viewModel.getDatos(binding.spAnioDetalle.selectedItem.toString())
    }

    private fun initObserves() {
        viewModel.estado.observe(this){ estado ->
            if(estado != null){
                dibujaUi(estado)
            }
        }
    }

    private fun dibujaUi(estado: VacasPendientesUiEstado) = with(binding){

        //1.- Recycler View
        miAdapter.submitList(estado.lista_vacaciones)

        // 2.- Texto de Vacas Atrasadas
        tvDiasPendientesCabecera.text = estado.vacas_atrasadas.toDias()

        //3.- Texto Vacas Disfrutadas
        tvTotalDisfrutadas.text = estado.vacas_disfrutadas.toDias()

        //4.- Texto Vacas Pendientes
        tvTotalPendientes.text = estado.vacas_pendientes.toDias()

        // 5.- Mensajes de error
        if(estado.msg_error != null){
            Toast.makeText(
                this@VacacionesDetalle,
                estado.msg_error,
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearError()
        }
    }

    private fun initListeners() = with(binding){
        spAnioDetalle.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strAno = p0?.getItemAtPosition(p2).toString()
                viewModel.getDatos(strAno)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun initRv() {
        with (binding.rvVacacionesDetalle){
                layoutManager = LinearLayoutManager(this@VacacionesDetalle)
                adapter = miAdapter
                setHasFixedSize(true)
            }
        val strAno = binding.spAnioDetalle.selectedItem.toString()
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