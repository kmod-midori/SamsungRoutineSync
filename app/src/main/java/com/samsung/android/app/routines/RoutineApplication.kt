package com.samsung.android.app.routines

import android.app.Application

class RoutineApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private lateinit var INSTANCE: RoutineApplication
        fun getInstance() = INSTANCE
    }
}