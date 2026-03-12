package com.samsung.android.app.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.samsung.android.app.routines.ui.theme.SamsungRoutineSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SamsungRoutineSyncTheme {
                val context = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(onClick = {
                            context.contentResolver.notifyChange(
                                "content://com.samsung.android.app.routines.modeinfoprovider/sleep_schedules/next_schedule".toUri(), null
                            )
                        }) {
                            Text("Notify schedule update")
                        }
                        Button(onClick = {
                            ModeInfoProvider.notifyModeStatusChanged(context)
                        }) {
                            Text("Turn on")
                        }
                        Button(onClick = {
                            ModeInfoProvider.notifyModeStatusChanged(context)
                        }) {
                            Text("Turn off")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SamsungRoutineSyncTheme {
        Greeting("Android")
    }
}