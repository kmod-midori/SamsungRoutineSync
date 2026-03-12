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
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.i(TAG, "query $uri")
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val result = when (method) {
            "get_mode" -> {
                Bundle().apply {
                    putLong("mode_id", 0)
                    putBoolean("mode_is_running", false)
                    putString("mode_running_reason_condition", "")
                }
            }
            "get_sleep_mode_info" -> {
                Bundle().apply {
                    putBoolean("is_dnd_on_enabled", true)
                    putBoolean("is_schedule_enabled", true)
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
                Bundle()
            }
            "set_sleep_schedule_status" -> {
                if (extras == null) return null
                Bundle()
            }
            else -> null
        }
        Log.i(TAG, "call $method $arg ${bundleToString(extras)} -> ${bundleToString(result)}")
        return result
    }

    fun bundleToString(bundle: Bundle?): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        if (bundle == null) return map
        for (key in bundle.keySet()) {
            map[key] = bundle.get(key)
        }
        return map
    }

    companion object {
        private val TAG = "ModeInfoProvider"

        private fun notifyChangeForUri(context: Context, uri: String) {
            context.contentResolver.notifyChange(uri.toUri(), null)
        }

        fun notifyModeStatusChanged(context: Context) {
            notifyChangeForUri(context, "content://com.samsung.android.app.routines.modeinfoprovider/mode_status")
        }

        fun notifySleepScheduleChanged(context: Context) {
            notifyChangeForUri(context, "content://com.samsung.android.app.routines.modeinfoprovider/sleep_schedules")
        }
    }
}