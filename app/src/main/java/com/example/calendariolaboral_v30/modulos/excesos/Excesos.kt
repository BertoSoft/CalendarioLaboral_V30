package com.example.calendariolaboral_v30.modulos.excesos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.databinding.ActivityExcesosBinding

class Excesos : AppCompatActivity() {

    private lateinit var binding: ActivityExcesosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcesosBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}