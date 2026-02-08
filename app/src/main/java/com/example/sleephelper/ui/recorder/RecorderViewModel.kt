package com.example.sleephelper.ui.recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephelper.data.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecorderUiState(
    val isRecording: Boolean = false,
    val recordingDuration: Long = 0L,
    val lastRecordedFile: String? = null
)

@HiltViewModel
class RecorderViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecorderUiState())
    val uiState: StateFlow<RecorderUiState> = _uiState.asStateFlow()

    fun startRecording(name: String) {
        viewModelScope.launch {
            try {
                audioRecorder.startRecording(name)
                _uiState.update { it.copy(isRecording = true) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecorder.stopRecording()
            _uiState.update { it.copy(isRecording = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.stopRecording()
    }
}
