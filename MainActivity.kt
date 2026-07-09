package com.lifesimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lifesimulator.ui.screens.*
import com.lifesimulator.ui.theme.Background
import com.lifesimulator.ui.theme.LifeSimulatorTheme

enum class Screen { INTRO, PROFILE, SCENARIO, LOADING, RESULTS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeSimulatorTheme {
                LifeSimulatorApp()
            }
        }
    }
}

@Composable
fun LifeSimulatorApp(vm: MainViewModel = viewModel()) {
    var screen by remember { mutableStateOf(Screen.INTRO) }

    // Observe simulation state changes
    val state = vm.simulationState
    LaunchedEffect(state) {
        when (state) {
            is SimulationState.Loading -> screen = Screen.LOADING
            is SimulationState.Success -> screen = Screen.RESULTS
            is SimulationState.Error -> screen = Screen.SCENARIO
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        when (screen) {
            Screen.INTRO -> IntroScreen(
                onStart = { screen = Screen.PROFILE }
            )
            Screen.PROFILE -> ProfileScreen(
                profile = vm.profile,
                onUpdate = { vm.updateProfile(it) },
                onToggleSkill = { vm.toggleSkill(it) },
                onBack = { screen = Screen.INTRO },
                onNext = { screen = Screen.SCENARIO }
            )
            Screen.SCENARIO -> ScenarioScreen(
                selected = vm.selectedScenario,
                error = (state as? SimulationState.Error)?.message,
                onSelect = { vm.selectScenario(it) },
                onBack = { screen = Screen.PROFILE },
                onRun = { vm.runSimulation() }
            )
            Screen.LOADING -> LoadingScreen(
                currentJob = vm.profile.currentJob,
                scenarioId = vm.selectedScenario ?: ""
            )
            Screen.RESULTS -> {
                val result = (state as? SimulationState.Success)?.result
                if (result != null) {
                    ResultsScreen(
                        result = result,
                        onReset = {
                            vm.reset()
                            screen = Screen.INTRO
                        }
                    )
                }
            }
        }
    }
}
