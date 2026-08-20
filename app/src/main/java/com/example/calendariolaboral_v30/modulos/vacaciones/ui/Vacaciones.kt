package com.example.calendariolaboral_v30.modulos.vacaciones.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesUiState
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesViewModel
import java.time.LocalDate

class Vacaciones : AppCompatActivity() {

    lateinit var binding: ActivityVacacionesBinding
    //private val utils = Utils()

    // 🟢 POR ESTA NUEVA LÍNEA CONECTADA AL APPCONTAINER:
    private val viewModel: VacacionesViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        VacacionesViewModel.Factory(
            vacacionesUseCase = app.appContainer.vacacionesUseCase,
            utils = app.appContainer.utils
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
            layoutManager = LinearLayoutManager(this@Vacaciones)
            adapter = miAdapter
            setHasFixedSize(true)
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
                   viewModel.onFechaInicioSeleccionada(ano, mes, dia)
               }
            }

            cardFechaFinContenedor.setOnClickListener {
                mostrarCalendario(2, "Selecciona una fecha para el final ..."){ ano, mes, dia ->
                    viewModel.onFechaFinalSeleccionada(ano, mes, dia)
                }
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
            spAnioVacaciones.setSelection(1)
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
        // 1. Pintar los textos en la pantalla de forma segura
        tvFechaInicio.text = estado.strFechaInicio.ifBlank { "-- / -- / ----" }
        tvFechaFin.text = estado.strFechaFinal.ifBlank { "-- / -- / ----" }

        // 2. Controlar la interactividad de la tarjeta final sin romper las esquinas
        setTarjetaFinalHabilitada(estado.isFechaFinHabilitada)

        // 2.5 Si la fechaFinal esta habilitada y su valor es "", lanzamos mostrarcalendario
        if(estado.isMostrarCalendario){
            mostrarCalendario(2, "Selecciona una fecha para el final ..."){ ano, mes, dia ->
                viewModel.onFechaFinalSeleccionada(ano, mes, dia)
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
                this@Vacaciones,
                mensaje,
                android.widget.Toast.LENGTH_SHORT
            ).show()
            viewModel.clearError()
        }
    }

    fun mostrarCalendario(indice: Int, strTitulo: String, onFechaSeleccionada: (Int, Int, Int) -> Unit) {
        val anoActual = LocalDate.now().year
        val mesActual = LocalDate.now().monthValue
        val diaActual = LocalDate.now().dayOfMonth

        val miDatePicker = DatePickerDialog(
            this,
            { _, anoSeleccion, mesSeleccion, diaSeleccion ->

                onFechaSeleccionada(anoSeleccion, mesSeleccion, diaSeleccion)
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