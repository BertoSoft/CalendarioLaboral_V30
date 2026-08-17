package com.example.calendariolaboral_v30


import android.app.Application
import com.example.calendariolaboral_v30.di.AppContainer

class MiAplicacion : Application() {

    val appContainer: AppContainer by lazy {
        AppContainer(this)
    }
}
