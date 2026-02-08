package com.example.sleephelper.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.sleephelper.data.model.Audio
import com.example.sleephelper.data.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val currentAudio: Audio? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: AudioRepository,
    private val player: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadAudio(audioId: String) {
        viewModelScope.launch {
            val audio = repository.getAudioById(audioId)
            if (audio != null) {
                _uiState.update { it.copy(currentAudio = audio, isLoading = true) }
                playAudio(audio)
            }
        }
    }

    private fun playAudio(audio: Audio) {
        // Basic playback logic using Media3
        // Ideally this should communicate with the Service via MediaController
        // But for MVP direct player access (via Hilt singleton) works for demonstration
        
        val mediaItem = MediaItem.fromUri(audio.url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        _uiState.update { it.copy(isPlaying = true, isLoading = false) }
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
            _uiState.update { it.copy(isPlaying = false) }
        } else {
            player.play()
            _uiState.update { it.copy(isPlaying = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Player lifecycle is managed by Service, not ViewModel
        // But we should stop updates here
    }
}
