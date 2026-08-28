package com.example.calendariolaboral_v30.modulos.calendario.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.databinding.ActivityCalendarioBinding

class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}