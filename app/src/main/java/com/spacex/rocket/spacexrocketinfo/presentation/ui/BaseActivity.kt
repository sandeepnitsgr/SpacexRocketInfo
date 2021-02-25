package com.spacex.rocket.spacexrocketinfo.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    abstract fun initInjector()
}