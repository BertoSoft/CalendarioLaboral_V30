package com.example.calendariolaboral_v30.modulos.calendario.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.databinding.ActivityCalendarioBinding
import com.example.calendariolaboral_v30.modulos.calendario.domain.model.Meses
import com.example.calendariolaboral_v30.modulos.calendario.ui.viewmodel.CalendarioUiEstado
import com.example.calendariolaboral_v30.modulos.calendario.ui.viewmodel.CalendarioViewModel
import java.time.LocalDate

class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding
    private val viewModel: CalendarioViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        CalendarioViewModel.Factory(
            utils = app.appContainer.utils,
            calendarioUseCase = app.appContainer.calendarioUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        initSp()
        initListeners()
        initRv()
        initObserves()
    }

    private fun initRv() {
        TODO("Not yet implemented")
    }

    private fun initObserves() {
        viewModel.estado.observe(this){ estado ->
            if(estado != null){
                dibujaUi(estado)
            }
        }
    }

    private fun dibujaUi(estado: CalendarioUiEstado) {
        // RecyclerView
        //miAdapter.submitList(estado.listaMes)

    }

    private fun initListeners() = with(binding){
        //sp ano
        spAnioCalendario.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strAno = p0?.getItemAtPosition(p2).toString()
                viewModel.spAnoClick(strAno)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        //spMes
        spMesCalendario.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val strMes = Meses.entries.getOrNull(p2)?.name ?:  ""
                viewModel.spMesClick(strMes)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    private fun initSp() {
        //Sp Meses
        val listaMeses = Meses.entries.map { it.name }
        val miAdapterMes = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaMeses
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        //sp Ano
        val ano = LocalDate.now().year
        val mes_actual = LocalDate.now().monthValue - 1

        val listaAnos = (ano +1).downTo(2022).map { it.toString() }
        val miAdapterAno = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaAnos
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Activar los sp's
        with(binding){
            spAnioCalendario.adapter = miAdapterAno
            if(spAnioCalendario.selectedItemPosition != 1) spAnioCalendario.setSelection(1)
            spMesCalendario.adapter = miAdapterMes
            spMesCalendario.setSelection(mes_actual)
        }
    }


}