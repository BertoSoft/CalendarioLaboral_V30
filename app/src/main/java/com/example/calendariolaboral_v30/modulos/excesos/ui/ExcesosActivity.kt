package com.example.calendariolaboral_v30.modulos.excesos.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.databinding.ActivityExcesosBinding
import com.example.calendariolaboral_v30.modulos.excesos.ui.viewmodel.ExcesosViewModel
import java.time.LocalDate

class ExcesosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcesosBinding
    private val viewModel: ExcesosViewModel by viewModels()

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
        TODO("Not yet implemented")
    }

    private fun initListeners() {
        TODO("Not yet implemented")
    }

    private fun initSp() {
        var ano = LocalDate.now().year
        ano ++


    }
}