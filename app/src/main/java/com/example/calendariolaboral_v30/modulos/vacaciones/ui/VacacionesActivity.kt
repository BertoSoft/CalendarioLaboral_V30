package com.example.calendariolaboral_v30.modulos.vacaciones.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.adapter.VacacionesAdapter
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesUiState
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesViewModel
import com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.VacacionesDetalle
import java.time.LocalDate

class VacacionesActivity : AppCompatActivity() {

    lateinit var binding: ActivityVacacionesBinding
    private val miAdapter = VacacionesAdapter()
    private val utils = Utils()

    private val viewModel: VacacionesViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        VacacionesViewModel.Factory(
            vacacionesUseCase = app.appContainer.vacacionesUseCase,
            utils = app.appContainer.utils,
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        initSp()
        initRecyclerView()
        initListeners()
        initObserves()
    }

    private fun initRecyclerView() {
        with(binding.rvVacaciones){
            layoutManager = LinearLayoutManager(this@VacacionesActivity)
            adapter = miAdapter
            setHasFixedSize(true)
        }
        miAdapter.onItemPulsado = { vacaciones ->
            viewModel.itemClick(vacaciones)
        }
        miAdapter.onItemDeletePulsado = { vacaciones ->
            viewModel.itemDeleteClick(vacaciones)
        }
    }

    private fun initObserves() {
        viewModel.estado.observe(this) { estado ->
            if (estado != null) {
                dibujaUi(estado)
            }
        }
    }

    private fun initListeners() =
        with(binding){
            cardFechaInicioContenedor.setOnClickListener {
               mostrarCalendario(1, "Selecciona una fecha de inicio..."){ ano, mes, dia ->
                   viewModel.tvFechaInicialClick(ano, mes, dia)
               }
            }
            cardFechaFinContenedor.setOnClickListener {
                mostrarCalendario(2, "Selecciona una fecha para el final ..."){ ano, mes, dia ->
                    viewModel.tvFechaFinalClick(ano, mes, dia)
                }
            }
            spAnioVacaciones.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    val ano = p0?.getItemAtPosition(p2).toString()
                    viewModel.spAnoClick(ano)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
            btnGuardarVacaciones.setOnClickListener {
                viewModel.btnGuardarClick()
            }
            btnDetalleVacaciones.setOnClickListener {
                val intent = Intent(this@VacacionesActivity, VacacionesDetalle::class.java)
                startActivity(intent)
            }
        }

    private fun initSp() {
        val ano = LocalDate.now().year
        val listAnos = ((ano +1)downTo 2022).map { it.toString() }
        val miAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listAnos
        )
        miAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding){
            spAnioVacaciones.adapter = miAdapter
            if(spAnioVacaciones.selectedItemPosition != 1){
                spAnioVacaciones.setSelection(1)
            }
        }
    }

    fun setTarjetaFinalHabilitada(isFechaFinHabilitada: Boolean) {
        with(binding){
            cardFechaFinContenedor.isEnabled = isFechaFinHabilitada
            if(isFechaFinHabilitada){
                cardFechaFinContenedor.setCardBackgroundColor(getColor(R.color.bg_fecha_active))
            }
            else{
                cardFechaFinContenedor.setCardBackgroundColor((getColor(R.color.bg_fecha_disabled)))
            }
        }
    }

    fun dibujaUi(estado: VacacionesUiState) = with(binding) {
        // 0.- RecyclerView
        miAdapter.submitList(estado.lista)

        // 1. Pintar los textos en la pantalla de forma segura
        var strFechaInicio = ""
        var strFechaFinal = ""
        if(estado.fecha_inicio != null){
            strFechaInicio = utils.fromLocalDateToFechaCorta(estado.fecha_inicio)
        }
        if(estado.fecha_final != null){
            strFechaFinal = utils.fromLocalDateToFechaCorta(estado.fecha_final)
        }
        tvFechaInicio.text = strFechaInicio.ifBlank { "-- / -- / ----" }
        tvFechaFin.text = strFechaFinal.ifBlank { "-- / -- / ----" }

        // 2. Controlar la interactividad de la tarjeta final sin romper las esquinas
        setTarjetaFinalHabilitada(estado.isFechaFinHabilitada)

        // 2.5 Si la fechaFinal esta habilitada y su valor es "", lanzamos mostrarcalendario
        if(estado.isMostrarCalendario){
            mostrarCalendario(2, "Selecciona una fecha para el final ..."){ ano, mes, dia ->
                viewModel.tvFechaFinalClick(ano, mes, dia)
                viewModel.clearMostrarCalendario()
            }
        }

        // 3. Controlar el estado del botón guardar
        btnGuardarVacaciones.isEnabled = estado.isBtnGuardarHabilitado
        if(btnGuardarVacaciones.isEnabled){
            btnGuardarVacaciones.setBackgroundColor(getColor(R.color.bg_btn_active))
        }
        else{
            btnGuardarVacaciones.setBackgroundColor(getColor(R.color.bg_btn_disabled))
        }

        // 4. Gestionar los mensajes de error de negocio si existen
        estado.msgError?.let { mensaje ->
            android.widget.Toast.makeText(
                this@VacacionesActivity,
                mensaje,
                android.widget.Toast.LENGTH_SHORT
            ).show()
            viewModel.clearError()
        }
    }

    private fun mostrarCalendario(indice: Int, strTitulo: String, onFechaSeleccionada: (Int, Int, Int) -> Unit) {
        val anoActual = LocalDate.now().year
        val mesActual = LocalDate.now().monthValue
        val diaActual = LocalDate.now().dayOfMonth

        val miDatePicker = DatePickerDialog(
            this,
            { _, anoSeleccion, mesSeleccion, diaSeleccion ->

                onFechaSeleccionada(anoSeleccion, mesSeleccion + 1, diaSeleccion)
            },
            anoActual,
            mesActual,
            diaActual
        )

        miDatePicker.setOnCancelListener {
            onFechaSeleccionada(-1, -1, -1)
        }
        miDatePicker.setTitle(strTitulo)
        miDatePicker.show()
    }
}