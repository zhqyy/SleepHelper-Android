package com.example.sleephelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephelper.data.model.Audio
import com.example.sleephelper.data.model.AudioCategory
import com.example.sleephelper.data.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val whiteNoiseList: List<Audio> = emptyList(),
    val storyList: List<Audio> = emptyList(),
    val recordingList: List<Audio> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AudioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Collect flows sequentially for simplicity in MVP
            repository.getAudiosByCategory(AudioCategory.WHITE_NOISE).collect { list ->
                _uiState.update { it.copy(whiteNoiseList = list) }
            }
            
            repository.getAudiosByCategory(AudioCategory.STORY).collect { list ->
                _uiState.update { it.copy(storyList = list) }
            }

            repository.getAudiosByCategory(AudioCategory.RECORDING).collect { list ->
                _uiState.update { it.copy(recordingList = list) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
