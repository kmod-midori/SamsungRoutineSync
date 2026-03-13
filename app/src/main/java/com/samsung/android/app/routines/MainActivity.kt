package com.samsung.android.app.routines

import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            MainActivityContent()
        }
    }
}

@Composable
fun MainActivityContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    SamsungRoutineSyncTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = {
                    ModeInfoProvider.notifySleepScheduleChanged(context)
                }) {
                    Text("Notify schedule update")
                }
                Button(onClick = {
                    ModeInfoProvider.notifyScheduleStatusChanged(context)
                }) {
                    Text("Notify schedule status update")
                }
                Button(onClick = {
                    ModeInfoProvider.sleepModeEnabled = true
                    ModeInfoProvider.notifyModeStatusChanged(context)
                }) {
                    Text("Turn on")
                }
                Button(onClick = {
                    ModeInfoProvider.sleepModeEnabled = false
                    ModeInfoProvider.notifyModeStatusChanged(context)
                }) {
                    Text("Turn off")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainActivityContent()
}