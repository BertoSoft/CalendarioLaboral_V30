package com.example.calendariolaboral_v30.modulos.excesos.ui

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.databinding.ActivityExcesosBinding
import com.example.calendariolaboral_v30.modulos.excesos.domain.usecase.ExcesosUseCase
import com.example.calendariolaboral_v30.modulos.excesos.ui.extensions.toHoras
import com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel.ExcesosUiEstado
import com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel.ExcesosViewModel
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.extensions.toDias
import java.time.LocalDate

class ExcesosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcesosBinding
    private val viewModel: ExcesosViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        ExcesosViewModel.Factory(
            excesosUseCase = app.appContainer.excesosUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcesosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
        initObserves()
        initListeners()
    }

    private fun initObserves() {
        viewModel.estado.observe(this){ estado ->
            dibujaUi(estado)
        }
    }

    fun dibujaUi(estado: ExcesosUiEstado) = with(binding){
        // 1.- Sabados Domingos y festivos
        tvValorSabados.text = estado.sabados.toDias()
        tvValorDomingos.text = estado.domingos.toDias()
        tvValorNacionales.text = estado.nacionales.toDias()
        tvValorAutonomicos.text = estado.autonomicos.toDias()
        tvValorLocales.text = estado.locales.toDias()
        tvValorConvenio.text = estado.convenio.toDias()

        // 2.- segundo bloque
        var strTexto = "(${estado.diasAno}-${estado.sabados}-${estado.domingos}-${estado.nacionales}-${estado.autonomicos}-${estado.locales}-${estado.convenio})x8h. = ${estado.horasTotales.toHoras()}"
        tvValorHoras.text = strTexto

        strTexto = "(22 Días x 8 h.) = 176 Horas."
        tvValorVacacionesExceso.text = strTexto


        strTexto = "${estado.horasTotales} h. - 176 h. = ${estado.horasTrabajo.toHoras()}"
        tvValorTotales.text = strTexto

        strTexto = "${estado.horasTrabajo} h. - 1752 h. = ${estado.horasSobrantes.toHoras()}"
        tvValorTotales1.text = strTexto

        tvDiasExcesoFinal.text = "Días de exceso: ${estado.diasSobrantes.toDias()}"
    }

    private fun initListeners() {
        with(binding){
            spAnioExcesos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    val ano = spAnioExcesos.selectedItem.toString().toInt()
                    viewModel.spAnoClick(ano)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }
    }

    private fun initSp() {
        var ano = LocalDate.now().year
        val listaAnos = ((ano + 1)downTo 2022).map{ it.toString() }
        val miAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaAnos
        )
        with(binding){
            spAnioExcesos.adapter = miAdapter
            if(spAnioExcesos.selectedItemPosition != 1){
                spAnioExcesos.setSelection(1)
            }
        }
    }


}