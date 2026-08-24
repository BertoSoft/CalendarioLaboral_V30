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
        initVacacionesPendientes()
        initRv()
        initListeners()
        initObserves()

    }

    private fun initObserves() {
        viewModel.estado.observe(this){ estado ->
            if(estado != null){
                dibujaUi(estado)
            }
        }
    }

    fun dibujaUi(estado: VacasPendientesUiEstado) = with(binding){
        //1.- Recycler View
        miAdapter.submitList(estado.lista_vacaciones)

        // 2.- Texto de Vacas Atrasadas
        var ano = spAnioDetalle.selectedItem.toString().toInt()
        ano --
        val strAno = ano.toString()
        val dato = estado.lista_vacas_pendientes.find { it.str_ano == strAno }
        var strTexto = "-- Días."
        var dias = 0
        if(dato != null && dato.dias > 0){
            dias = dato.dias
            strTexto = "$dias Días."
        }
        tvDiasPendientesCabecera.text = strTexto
        tvDiasPendientesCabecera.tag = dias

        //3.- Dias Consumidos este año
        val lista = estado.lista_vacaciones
        var diasConsumidos = 0
        for(lista in lista){
            diasConsumidos += lista.total_dias
        }
        strTexto = "-- Días."
        if(diasConsumidos > 0){
            strTexto = "$diasConsumidos Días"
        }
        tvTotalDisfrutadas.text = strTexto
        tvTotalDisfrutadas.tag = diasConsumidos

        //4.- Dias No Consumidos
        val diasAtrasados = tvDiasPendientesCabecera.tag as Int
        val diasDisfrutados = tvTotalDisfrutadas.tag as Int
        dias = 0
        strTexto = "-- Días"
        dias = (22 + diasAtrasados ) - diasDisfrutados
        if(dias > 0){
            strTexto = "$dias Días"
        }
        tvTotalPendientes.text = strTexto
        tvTotalPendientes.tag = dias

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
                viewModel.getAllVacaciones(strAno)
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
        viewModel.getAllVacaciones(strAno)
    }

    private fun initVacacionesPendientes() {
        val strAno = binding.spAnioDetalle.selectedItem.toString()
        viewModel.getDiasVacasPendientes()
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