package com.samsung.android.app.routines

data class SleepModeState(
    val running: Boolean,
    val source: Source,
    val timestamp: Long,
) {
    enum class Source {
        App,
        Tasker,
        Watch,
    }
}
