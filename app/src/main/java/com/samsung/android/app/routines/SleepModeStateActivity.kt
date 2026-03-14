package com.samsung.android.app.routines

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.joaomgcd.taskerpluginlibrary.condition.TaskerPluginRunnerConditionState
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.extensions.requestQuery
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultCondition
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionSatisfied
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultConditionUnsatisfied

class SleepModeStateActivity : ComponentActivity(), TaskerPluginConfig<SleepModeStateInput> {
    override val context get() = applicationContext!!
    override val inputForTasker get() = TaskerInput(SleepModeStateInput())
    override fun assignFromInput(input: TaskerInput<SleepModeStateInput>) {}

    private val helper by lazy { SleepModeStatHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper.onCreate()
        helper.finishForTasker()
    }
}

@TaskerInputRoot
class SleepModeStateInput

class SleepModeStateOutput

class SleepModeStatHelper(config: TaskerPluginConfig<SleepModeStateInput>) :
    TaskerPluginConfigHelper<SleepModeStateInput, SleepModeStateOutput, SleepModeStateRunner>(config) {
    override val runnerClass = SleepModeStateRunner::class.java
    override val inputClass = SleepModeStateInput::class.java
    override val outputClass = SleepModeStateOutput::class.java
}

class SleepModeStateRunner :
    TaskerPluginRunnerConditionState<SleepModeStateInput, SleepModeStateOutput>() {
    override fun getSatisfiedCondition(
        context: Context,
        input: TaskerInput<SleepModeStateInput>,
        update: Unit?
    ): TaskerPluginResultCondition<SleepModeStateOutput> {
        return if (RoutineApplication.getInstance().sleepMode.value.running) {
            TaskerPluginResultConditionSatisfied(context)
        } else {
            TaskerPluginResultConditionUnsatisfied()
        }
    }

    companion object {
        fun notifyTasker(context: Context) {
            SleepModeStateActivity::class.java.requestQuery(context)
        }
    }
}