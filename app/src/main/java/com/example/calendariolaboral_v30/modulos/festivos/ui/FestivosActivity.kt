package com.example.calendariolaboral_v30.modulos.festivos.ui


import android.app.DatePickerDialog
import android.content.res.ObbInfo
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ActivityFestivosBinding
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.ui.adapter.FestivosAdapter
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toStringRes
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toTipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.ui.viewmodel.FestivosViewModel
import java.time.LocalDate

class FestivosActivity : AppCompatActivity() {

    private val utils = Utils()
    private lateinit var binding: ActivityFestivosBinding
    private val miAdapter = FestivosAdapter{ itemPulsado ->
        viewModel.itemFestivoPulsado(itemPulsado.fecha)
        viewModel.setModoEdicion(true)
    }

    // 🟢 POR ESTA NUEVA LÍNEA CONECTADA AL APPCONTAINER:
    private val viewModel: FestivosViewModel by viewModels {
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        FestivosViewModel.Factory(
            useCase = app.appContainer.festivosUseCase,
            utils = utils // Aprovecha el objeto 'utils' que ya declaraste arriba en la actividad
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
        ivNuevo.setOnClickListener {
           mostrarCalendario { setModoEdicion(it) }
        }
        spAnio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val ano = p0?.getItemAtPosition(p2).toString()
                viewModel.getAllFestivos(ano)
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
        btnGuardar.setOnClickListener {
            val fecha = utils.fromFechaCOrtaToLocalDate(binding.tvFecha.text.toString())
            val tipoFestivo = binding.spFestivo.selectedItem.toString().toTipoFestivo()
            viewModel.setFestivo(fecha, tipoFestivo)
        }
    }

    private fun initUi() {
        initSp()
        initRecyclerView()
        setModoEdicion(false)
        binding.cardEliminarBoton.isVisible = false
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
        val tipoFestivos = TipoFestivo.entries.map { tipo ->
            getString(tipo.toStringRes())
        }

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tipoFestivos
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
    }

    private fun setModoEdicion(isEdicion: Boolean) = with(binding){
        spFestivo.isEnabled = isEdicion
        btnGuardar.isEnabled = isEdicion
        tvFecha.isEnabled = isEdicion

        if (isEdicion) {
            tvFecha.setTextColor(getColor(R.color.text_main))
            cardFechaContenedor.setCardBackgroundColor(getColor(R.color.bg_fecha_active))
            btnGuardar.setBackgroundColor(getColor(R.color.bg_btn_active))

        } else {
            tvFecha.setTextColor(getColor(R.color.text_disabled))
            cardFechaContenedor.setCardBackgroundColor(getColor(R.color.bg_fecha_disabled))
            btnGuardar.setBackgroundColor(getColor(R.color.bg_btn_disabled))
        }
    }

    private fun mostrarCalendario(isAceptar: (Boolean) -> Unit){
        val anoActual = Calendar.getInstance().get(Calendar.YEAR)
        val mesActual = Calendar.getInstance().get(Calendar.MONTH)
        val diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            {_, anoSeleccion, mesSeleccion, diaSeleccion ->
                        val mesCorregido = mesSeleccion + 1
                        val fechaFormateada = utils.fromLocalDatetoFechaCorta(LocalDate.of(anoSeleccion, mesCorregido, diaSeleccion))
                        binding.tvFecha.text = fechaFormateada
                        isAceptar(true)
            },
            anoActual,
            mesActual,
            diaActual
        )

        datePicker.setOnCancelListener {
            isAceptar(false)
        }

        datePicker.show()
    }

    // Función auxiliar para mostrar mensajes rápidos en pantalla
    private fun showMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

}
