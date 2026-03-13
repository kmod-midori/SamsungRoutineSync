package com.samsung.android.app.routines

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerActionNoOutput
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelperNoOutput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import com.samsung.android.app.routines.ui.theme.SamsungRoutineSyncTheme
import kotlinx.coroutines.flow.MutableStateFlow

class SleepModeActionActivity : ComponentActivity(), TaskerPluginConfig<SleepModeInput> {
    override val context get() = applicationContext!!
    override val inputForTasker get() = TaskerInput(SleepModeInput(enable = enableFlow.value))
    override fun assignFromInput(input: TaskerInput<SleepModeInput>) = input.regular.run {
        enableFlow.value = enable
    }

    private val helper by lazy { SleepModeActionHelper(this) }

    private val enableFlow = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper.onCreate()
        enableEdgeToEdge()
        setContent {
            val enabled by enableFlow.collectAsState()
            SleepModeActionActivityContent(
                enable = enabled,
                setEnable = { enableFlow.value = it },
                onDone = { helper.finishForTasker() },
            )
        }
    }
}

@Composable
fun SleepModeActionActivityContent(
    enable: Boolean,
    setEnable: (Boolean) -> Unit,
    onDone: () -> Unit,
) {
    SamsungRoutineSyncTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Text("Enabled")
                    Switch(checked = enable, onCheckedChange = setEnable)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(onClick = { onDone() }) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@TaskerInputRoot
class SleepModeInput @JvmOverloads constructor(
    @field:TaskerInputField("enable") var enable: Boolean = false
)

class SleepModeActionHelper(config: TaskerPluginConfig<SleepModeInput>) :
    TaskerPluginConfigHelperNoOutput<SleepModeInput, SleepModeActionRunner>(config) {
    override val runnerClass = SleepModeActionRunner::class.java
    override val inputClass = SleepModeInput::class.java
    override val addDefaultStringBlurb = false
    override fun addToStringBlurb(input: TaskerInput<SleepModeInput>, blurbBuilder: StringBuilder) {
        blurbBuilder.append("Enable sleep mode: ${input.regular.enable}")
    }
}

class SleepModeActionRunner : TaskerPluginRunnerActionNoOutput<SleepModeInput>() {
    override fun run(
        context: Context,
        input: TaskerInput<SleepModeInput>
    ): TaskerPluginResult<Unit> {
        ModeInfoProvider.sleepModeEnabled = input.regular.enable
        ModeInfoProvider.notifyModeStatusChanged(context)
        return TaskerPluginResultSucess(Unit)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SleepModeActionActivityContent(
        enable = false,
        setEnable = {},
        onDone = {},
    )
}