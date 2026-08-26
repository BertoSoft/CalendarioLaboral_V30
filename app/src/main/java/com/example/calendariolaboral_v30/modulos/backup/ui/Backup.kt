package com.example.calendariolaboral_v30.modulos.backup.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.core.data.MiSqliteHelper
import com.example.calendariolaboral_v30.databinding.ActivityBackupBinding
import com.example.calendariolaboral_v30.modulos.backup.ui.viewmodel.BackupViewModel

class Backup : AppCompatActivity() {

    private lateinit var binding: ActivityBackupBinding
    private lateinit var dbHelper: MiSqliteHelper
    private val viewModel: BackupViewModel by viewModels{
        val app = application as com.example.calendariolaboral_v30.MiAplicacion
        BackupViewModel.Factory(
            aplicacion = app.appContainer.aplicacion,
            backupUseCase = app.appContainer.backupUseCase
        )
    }

    private val dialogoGuardar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let { uri -> viewModel.guardarCopia(uri) }
        }
    }

    private val dialogoAbrir = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            result.data?.data?.let { uri -> viewModel.abrirCopia(uri) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MiSqliteHelper(this)
        initListeners()
        initObservers()

    }

    private fun initObservers() {
        viewModel.estado.observe(this){ estado ->
            //Boton Exportar
            if(estado.isExportar){
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/x-sqlite3"
                    putExtra(Intent.EXTRA_TITLE, "copia_Calendario.db")
                }
                dialogoGuardar.launch(intent)
                viewModel.clearExportar()
            }
            // Boton Importar
            if(estado.isImportar){
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                dialogoAbrir.launch(intent)
                viewModel.clearImportar()
            }

            // Mensaje de error
            if(estado.msgError != null){
                Toast.makeText(this, estado.msgError, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun initListeners() = with(binding){
        btnGuardarCopia.setOnClickListener {
            viewModel.getExportar()
        }
        btnAbrirCopia.setOnClickListener {
            viewModel.getImportar()
        }
    }


}