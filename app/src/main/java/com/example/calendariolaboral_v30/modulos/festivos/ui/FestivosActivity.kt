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
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivos
import com.example.calendariolaboral_v30.modulos.festivos.ui.adapter.FestivosAdapter
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toStringRes
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toTipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel.FestivosUiEstado
import com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel.FestivosViewModel
import java.time.LocalDate

class FestivosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFestivosBinding
    private val miAdapter = FestivosAdapter()
    private val viewModel: FestivosViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        FestivosViewModel.Factory(
            useCase = app.appContainer.festivosUseCase,
        )
    }
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initListeners()
        initObserves()
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
                viewModel.spAnoClick(ano)
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
                val tipos = TipoFestivos.entries
                val tipoFestivo = tipos[p2]
                viewModel.spFestivosClick(tipoFestivo.name)

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        tvFecha.setOnClickListener {
            mostrarCalendario("Selecciona una fecha festiva...") { ano, mes, dia ->
                viewModel.tvFechaClick(ano, mes, dia)
            }
        }
        btnGuardar.setOnClickListener {
            viewModel.btnGuardarClick()
        }
        btnAtrasFestivos.setOnClickListener {
            finish()
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
        miAdapter.onItemPulsado = { festivo ->
            viewModel.itemClick(festivo)
        }
        miAdapter.onItemDeletePulsado = { festivo ->
            viewModel.itemDeleteClick(festivo)
        }
    }

    private fun initSp() {
        initSpAno()
        initSpFestivos()
    }

    private fun initObserves() {
        viewModel.estado.observe(this){ estado ->
            if(estado != null){
                dibujaUi(estado)
            }
        }
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
        val listaStrFestivos = TipoFestivos.entries.map { tipoFestivo ->
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

    private fun dibujaUi(estado: FestivosUiEstado) = with(binding){
        //0 RecyclerView
        miAdapter.submitList(estado.listaFestivos)

        //1.- Texto Fecha y color
        var strFechaLarga: String = ""
        if(estado.fecha != null){
            strFechaLarga = utils.fromLocalDateToFechaLarga(estado.fecha)
        }
        tvFecha.text = strFechaLarga.ifBlank { "Seleccionar Fecha 📅" }

        //2.- SpFestivos
        spFestivo.isEnabled = (estado.isSpTipoActivo)
        val indice = TipoFestivos.entries.find { it.name == estado.strTipo }?.ordinal
        if(spFestivo.isEnabled){
            if(indice != null && spFestivo.selectedItemPosition != indice){
                spFestivo.setSelection(indice)
            }
        }
        else if(spFestivo.selectedItemPosition != indice){
                spFestivo.setSelection(0)
        }

        //3.- Boton Guardar
        setBtnGuardarHabilitado(estado.isBtnGuardarActivo)
    }

    private fun setBtnGuardarHabilitado(isBtnHabilitado: Boolean){
        binding.btnGuardar.isEnabled = isBtnHabilitado

        if(isBtnHabilitado){
            binding.btnGuardar.setBackgroundColor(getColor(R.color.bg_fecha_active))
        }
        else{
            binding.btnGuardar.setBackgroundColor(getColor(R.color.bg_fecha_disabled))
        }
    }

    private fun mostrarCalendario(strTitulo: String, onFechaSeleccionada: (Int, Int, Int) -> Unit ){
        val anoActual = Calendar.getInstance().get(Calendar.YEAR)
        val mesActual = Calendar.getInstance().get(Calendar.MONTH)
        val diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            {_, anoSeleccion, mesSeleccion, diaSeleccion ->
                onFechaSeleccionada(anoSeleccion, mesSeleccion + 1, diaSeleccion
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
