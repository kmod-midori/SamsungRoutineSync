package com.samsung.android.app.routines

import android.app.Application
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers

class RoutineApplication : Application() {
    val sleepMode = MutableStateFlow(
        SleepModeState(
            running = false,
            source = SleepModeState.Source.App,
            timestamp = System.currentTimeMillis()
        )
    )

    private val prefs by lazy { getSharedPreferences("routine", MODE_PRIVATE) }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        sleepMode.value = SleepModeState(
            running = prefs.getBoolean("sleepMode", false),
            source = SleepModeState.Source.valueOf(
                prefs.getString("sleepModeSource", SleepModeState.Source.App.name)!!
            ),
            timestamp = prefs.getLong("sleepModeTimestamp", System.currentTimeMillis()),
        )

        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.IO) {
            sleepMode.collect {
                Log.i(TAG, "sleepMode: $it")

                prefs.edit {
                    putBoolean("sleepMode", it.running)
                    putString("sleepModeSource", it.source.name)
                    putLong("sleepModeTimestamp", it.timestamp)
                }
                Log.i(TAG, "Notifying watch")
                ModeInfoProvider.notifyModeStatusChanged(this@RoutineApplication)
                Log.i(TAG, "Notifying Tasker")
                SleepModeStateRunner.notifyTasker(this@RoutineApplication)
            }
        }
    }

    fun updateSleepMode(source: SleepModeState.Source, running: Boolean) {
        sleepMode.value = SleepModeState(
            running = running,
            source = source,
            timestamp = System.currentTimeMillis()
        )
    }

    fun updateSleepModeFromPhone(running: Boolean) {
        updateSleepMode(SleepModeState.Source.App, running)
    }

    fun updateSleepModeFromWatch(running: Boolean) {
        updateSleepMode(SleepModeState.Source.Watch, running)
    }

    fun updateSleepModeFromTasker(running: Boolean) {
        updateSleepMode(SleepModeState.Source.Tasker, running)
    }

    companion object {
        private const val TAG = "RoutineApplication"
        private lateinit var INSTANCE: RoutineApplication
        fun getInstance() = INSTANCE
    }
}