package com.example.calendariolaboral_v30.modulos.festivos.ui


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.ui.adapter.FestivosAdapter
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toStringRes
import com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel.FestivosViewModel
import java.time.LocalDate

class FestivosActivity : AppCompatActivity() {

    private val utils = Utils()
    private lateinit var binding: ActivityFestivosBinding
    private var fechaSeleccionada: LocalDate? = null
    private val miAdapter = FestivosAdapter(
        onItemPulsado = { festivo ->
            viewModel.onItemPulsado(festivo)
        },
        onItemDeletePulsado = { festivo ->
            viewModel.onItemDeletePulsado(festivo)
        }
    )

    // 🟢 POR ESTA NUEVA LÍNEA CONECTADA AL APPCONTAINER:
    private val viewModel: FestivosViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        FestivosViewModel.Factory(
            useCase = app.appContainer.festivosUseCase,
            utils = app.appContainer.utils
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initListeners()
        initObservers()
    }

    private fun initListeners() = with(binding){
        spAnio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val ano = p0?.getItemAtPosition(p2).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        spFestivo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        tvFecha.setOnClickListener {
            mostrarCalendario("Selecciona una fecha festiva...") { ano, mes, dia ->
                viewModel.onFechaSeleccionada(ano, mes, dia)
            }
        }
        btnGuardar.setOnClickListener {
            val tipo = TipoFestivo.entries[spFestivo.selectedItemPosition]
            val fecha = fechaSeleccionada
            if(fecha != null){
                viewModel.setFestivo(DatosFestivos(
                    -1,
                    fecha,
                    tipo
                ))
            }

        }
    }

    private fun initUi() {
        initSp()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding.rvFestivos){
            layoutManager = LinearLayoutManager(this@FestivosActivity)
            adapter = miAdapter
            setHasFixedSize(true)
        }
    }

    private fun initSp() {
        initSpAno()
        initSpFestivos()
    }

    private fun initSpAno() {
        val ano = Calendar.getInstance().get(Calendar.YEAR)

        // Desde una año despues del actual hasta 2022
        val listaAnos = ((ano + 1) downTo 2022).map { it.toString() }

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaAnos
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding){
            spAnio.adapter = arrayAdapter
            spAnio.setSelection(1)
        }
    }

    private fun initSpFestivos() {
        val listaStrFestivos = TipoFestivo.entries.map { tipoFestivo ->
            getString(tipoFestivo.toStringRes())
        }

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaStrFestivos
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spFestivo.adapter = arrayAdapter
    }

    private fun initObservers() {
        viewModel.listaFestivos.observe(this){ lista ->
            miAdapter.submitList(lista)
        }

        viewModel.msgError.observe(this){ msg ->
            msg?.let {
                showMensaje(it)
            }
        }

        viewModel.isCargando.observe(this){ isCargando ->
            if(isCargando){
                // Aqui un progress de carga visible
            }
            else{
                // aqui invisible
            }
        }

        viewModel.isEdicionEstadoUi.observe(this){ isEdicion ->
        }

        viewModel.itemPulsadoEstadoUi.observe(this){ estadoFestivo ->
            with(binding){
                if(estadoFestivo != null) {
                    // Aqui ira el codigo que selecciona un registro
                    val strFechaLarga = utils.fromLocalDateToFechaLarga(estadoFestivo.festivo.fecha)
                    val indice = estadoFestivo.festivo.tipo.ordinal

                    tvFecha.text = strFechaLarga
                    spFestivo.setSelection(indice)
                    fechaSeleccionada = estadoFestivo.festivo.fecha
                }
                else{
                    binding.tvFecha.text = getString(R.string.texto_elegir_fecha)
                    fechaSeleccionada = null
                }
            }
        }

        viewModel.itemDeletePulsadoEstadoUi.observe(this){ festivo ->
            festivo?.let {
                if(it.isDelete){
                    showMensaje("Registro borrado con exito")
                    viewModel.clearDeleteObsever()
                }
            }
        }
    }



    private fun mostrarCalendario(strTitulo: String, onFechaSeleccionada: (Int, Int, Int) -> Unit ){
        val anoActual = Calendar.getInstance().get(Calendar.YEAR)
        val mesActual = Calendar.getInstance().get(Calendar.MONTH)
        val diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            {_, anoSeleccion, mesSeleccion, diaSeleccion ->
                onFechaSeleccionada(anoSeleccion, mesSeleccion, diaSeleccion
                )
            },
            anoActual,
            mesActual,
            diaActual
        )

        datePicker.setOnCancelListener {
            onFechaSeleccionada(-1, -1, -1)
        }

        datePicker.setTitle(strTitulo)
        datePicker.show()
    }

    // Función auxiliar para mostrar mensajes rápidos en pantalla
    private fun showMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

}
