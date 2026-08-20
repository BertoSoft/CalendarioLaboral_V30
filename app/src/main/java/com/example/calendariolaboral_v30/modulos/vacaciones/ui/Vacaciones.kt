package com.example.calendariolaboral_v30.modulos.vacaciones.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral_v30.databinding.ItemFestivosBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesUiState
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.viewmodel.VacacionesViewModel
import java.time.LocalDate

class Vacaciones : AppCompatActivity() {

    lateinit var binding: ActivityVacacionesBinding
    private val utils = Utils()

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
        initListeners()
        initObserves()
        binding.cardFechaFinContenedor.isEnabled = false
        binding.btnGuardarVacaciones.isEnabled = false
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
                mostrarCalendario(1,"Fecha de inicio de las vacaciones"){ isAceptar ->
                    if(isAceptar){
                        cardFechaFinContenedor.isEnabled = true
                        mostrarCalendario(2,"Fecha de final de las vacaciones"){ isAceptar ->
                            if(isAceptar){
                                btnGuardarVacaciones.isEnabled = true
                            }
                        }
                    }
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

    }

    fun dibujaUi(estado: VacacionesUiState) = with(binding) {
        // 1. Pintar los textos en la pantalla de forma segura
        tvFechaInicio.text = estado.strFechaInicio.ifBlank { "-- / -- / ----" }
        tvFechaFin.text = estado.strFechaFin.ifBlank { "-- / -- / ----" }

        // 2. Controlar la interactividad de la tarjeta final sin romper las esquinas
        setTarjetaFinalHabilitada(estado.isFechaFinHabilitada)

        // 3. Controlar el estado del botón guardar
        btnGuardarVacaciones.isEnabled = estado.isBtnGuardarHabilitado

        // 4. Gestionar los mensajes de error de negocio si existen
        estado.msgError?.let { mensaje ->
            android.widget.Toast.makeText(
                this@Vacaciones,
                mensaje,
                android.widget.Toast.LENGTH_SHORT
            ).show()
            // Opcional: puedes llamar a un método en el VM para limpiar el error una vez mostrado
        }
    }

    fun mostrarCalendario(indice: Int, strTitulo: String, isAceptar: (Boolean) -> Unit) {
        val anoActual = LocalDate.now().year
        val mesActual = LocalDate.now().monthValue
        val diaActual = LocalDate.now().dayOfMonth

        val miDatePicker = DatePickerDialog(
            this,
            { _, anoSeleccion, mesSeleccion, diaSeleccion ->

                val mesCorregido = mesSeleccion + 1
                val fecha = LocalDate.of(anoSeleccion, mesCorregido, diaSeleccion)
                val strFecha = utils.fromLocalDateToFechaCorta(fecha)
                if(indice == 1){
                    viewModel.onFechaInicioSeleccionada(strFecha)
                }
                else{
                    viewModel.onFechaFinalSeleccionada(strFecha)
                }
                isAceptar(true)
            },
            anoActual,
            mesActual,
            diaActual
        )

        miDatePicker.setOnCancelListener {
            isAceptar(false)
        }
        miDatePicker.setTitle(strTitulo)
        miDatePicker.show()
    }
}