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
import com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel.ExcesosViewModel
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
                    viewModel.spAnoClick(spAnioExcesos.selectedItem.toString())
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