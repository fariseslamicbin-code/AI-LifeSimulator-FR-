package com.lifesimulator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifesimulator.data.SimulationRepository
import com.lifesimulator.data.SimulationResult
import com.lifesimulator.data.UserProfile
import kotlinx.coroutines.launch

sealed class SimulationState {
    object Idle : SimulationState()
    object Loading : SimulationState()
    data class Success(val result: SimulationResult) : SimulationState()
    data class Error(val message: String) : SimulationState()
}

class MainViewModel : ViewModel() {

    private val repository = SimulationRepository()

    var profile by mutableStateOf(UserProfile())
        private set

    var selectedScenario by mutableStateOf<String?>(null)
        private set

    var simulationState by mutableStateOf<SimulationState>(SimulationState.Idle)
        private set

    fun updateProfile(update: UserProfile.() -> UserProfile) {
        profile = profile.update()
    }

    fun toggleSkill(skill: String) {
        profile = if (skill in profile.skills) {
            profile.copy(skills = profile.skills - skill)
        } else {
            profile.copy(skills = profile.skills + skill)
        }
    }

    fun selectScenario(id: String) {
        selectedScenario = id
    }

    fun runSimulation() {
        val scenario = selectedScenario ?: return
        simulationState = SimulationState.Loading

        viewModelScope.launch {
            repository.runSimulation(
                profile = profile,
                scenarioId = scenario,
                onResult = { result ->
                    simulationState = SimulationState.Success(result)
                },
                onError = { error ->
                    simulationState = SimulationState.Error(error)
                }
            )
        }
    }

    fun reset() {
        profile = UserProfile()
        selectedScenario = null
        simulationState = SimulationState.Idle
    }
}
