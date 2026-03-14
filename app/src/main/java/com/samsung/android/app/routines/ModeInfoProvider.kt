package com.samsung.android.app.routines

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.tooling.data.SourceContext
import androidx.core.net.toUri
import org.json.JSONObject

class ModeInfoProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val result = when (method) {
            "get_mode" -> {
                val isSleepModeRunning = RoutineApplication.getInstance().sleepMode.value.running

                Bundle().apply {
                    putLong("mode_id", SLEEP_MODE_ID)
                    putBoolean("mode_is_running", isSleepModeRunning)
                    putString("mode_running_reason_condition", "")
                }
            }
            "get_sleep_mode_info" -> {
                Bundle().apply {
                    putBoolean("is_dnd_on_enabled", true)
                    putBoolean("is_schedule_enabled", false)
                }
            }
            "get_mode_resources" -> {
                Bundle().apply {
                    putString("mode_display_name", "Sleep")
                }
            }
            "get_next_sleep_schedule" -> {
                Bundle().apply {
                    val schedule = JSONObject().apply {
                        put("start_time", 100)
                        put("end_time", 820)
                        put("repeat_days_of_week", 0x11111111)
                    }
                    putString("next_sleep_schedule", schedule.toString())
                }
            }
            "set_mode" -> {
                if (extras == null) return null
                val modeIsRunning = extras.getBoolean("mode_is_running", false)
                val modeId = extras.getLong("mode_id", 0)
                Log.i(TAG, "set_mode $modeId $modeIsRunning")

                if (modeId == SLEEP_MODE_ID) {
                    RoutineApplication.getInstance().updateSleepModeFromWatch(modeIsRunning)
                }

                Bundle()
            }
            "set_sleep_schedule_status" -> {
                Bundle()
            }
            else -> null
        }
        Log.i(TAG, "call $method $arg ${bundleToString(extras)} -> ${bundleToString(result)}")
        return result
    }

    @Suppress("DEPRECATION")
    fun bundleToString(bundle: Bundle?): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        if (bundle == null) return map
        for (key in bundle.keySet()) {
            map[key] = bundle.get(key)
        }
        return map
    }

    companion object {
        private const val TAG = "ModeInfoProvider"
        private const val SLEEP_MODE_ID = 100L

        private fun notifyChangeForUri(context: Context, uri: String) {
            context.contentResolver.notifyChange(uri.toUri(), null)
        }

        fun notifyModeStatusChanged(context: Context) {
            notifyChangeForUri(context, "content://com.samsung.android.app.routines.modeinfoprovider/mode_status")
        }

        fun notifySleepScheduleChanged(context: Context) {
            notifyChangeForUri(context, "content://com.samsung.android.app.routines.modeinfoprovider/sleep_schedules/next_schedule")
        }

        fun notifyScheduleStatusChanged(context: Context) {
            notifyChangeForUri(context, "content://com.samsung.android.app.routines.modeinfoprovider/sleep_schedules/status")
        }
    }
}