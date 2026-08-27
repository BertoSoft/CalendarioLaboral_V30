package com.example.calendariolaboral_v30.modulos.home.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.databinding.ActivityMainBinding
import com.example.calendariolaboral_v30.modulos.backup.ui.Backup
import com.example.calendariolaboral_v30.modulos.excesos.ui.ExcesosActivity
import com.example.calendariolaboral_v30.modulos.festivos.ui.FestivosActivity
import com.example.calendariolaboral_v30.modulos.home.domain.model.DatosMenu
import com.example.calendariolaboral_v30.modulos.home.ui.viewmodel.MainViewModel
import com.example.calendariolaboral_v30.modulos.vacaciones.ui.VacacionesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        initObservers()
    }

    //##############################################################3
    // Funciones de configuracion App
    //###############################################################
    private fun initListeners() {
        with(binding) {
            cardExcesoJornadas.setOnClickListener {
                viewModel.tarjetaPulsada(DatosMenu.EXCESOS)
            }
            cardVacaciones.setOnClickListener {
                viewModel.tarjetaPulsada(DatosMenu.VACACIONES)
            }
            cardFestivos.setOnClickListener {
                viewModel.tarjetaPulsada(DatosMenu.FESTIVOS)
            }
            cardBackup.setOnClickListener {
                viewModel.tarjetaPulsada(DatosMenu.BACKUP)
            }
            cardCalendario.setOnClickListener {
                viewModel.tarjetaPulsada(DatosMenu.CALENDARIO)
            }
            cardSalir.setOnClickListener {
                viewModel.ejecutarSalir()
            }
        }
    }

    private fun initObservers() {
        // 1. Escuchamos cuando el ViewModel nos ordene navegar a una pantalla
        viewModel.navegarAModulo.observe(this){ menu ->
            menu?.let {
                when (it) {
                    DatosMenu.FESTIVOS -> {
                        val intent = Intent(this, FestivosActivity::class.java)
                        startActivity(intent)
                    }

                    DatosMenu.VACACIONES ->{
                        val intent = Intent(this, VacacionesActivity::class.java)
                        startActivity(intent)
                    }

                    DatosMenu.EXCESOS ->{
                        val intent = Intent(this, ExcesosActivity::class.java)
                        startActivity(intent)
                    }

                    DatosMenu.BACKUP ->{
                        val intent = Intent(this, Backup::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        showMensaje("Módulo ${it.name} en desarrollo")
                    }
                }
                // Crucial en MVVM: Le confirmamos al ViewModel que la navegación se ha completado
                viewModel.navegacionCompletada()
            }
        }

        // 2. Escuchamos cuando el ViewModel nos ordene cerrar la aplicación
        viewModel.eventoSalir.observe(this){ salir ->
            if(salir){
                finishAffinity()
            }
        }

    }

    //##########################################################33
    // Funciones Auxiliares Helpers
    //#########################################################
    private fun showMensaje(mensaje: String){
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }
}