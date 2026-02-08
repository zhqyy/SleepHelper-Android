package com.example.sleephelper.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephelper.data.model.Audio
import com.example.sleephelper.data.model.AudioCategory

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAudioClick: (String) -> Unit,
    onRecordClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Reload data when returning to screen to catch new recordings
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("晚安宝贝") },
                actions = {
                    IconButton(onClick = onRecordClick) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "录音",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // White Noise Section
            item {
                SectionHeader(title = "白噪音", color = MaterialTheme.colorScheme.primary)
            }
            items(uiState.whiteNoiseList) { audio ->
                AudioItem(audio = audio, onClick = { onAudioClick(audio.id) })
            }

            // Stories Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = "睡前故事", color = MaterialTheme.colorScheme.secondary)
            }
            items(uiState.storyList) { audio ->
                AudioItem(audio = audio, onClick = { onAudioClick(audio.id) })
            }

            // Recordings Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = "我的录音", color = MaterialTheme.colorScheme.tertiary)
            }
            if (uiState.recordingList.isEmpty()) {
                item {
                    EmptyStateMessage(message = "还没有录音哦，快去给宝宝录个故事吧！")
                }
            } else {
                items(uiState.recordingList) { audio ->
                    AudioItem(audio = audio, onClick = { onAudioClick(audio.id) })
                }
            }
            
            // Bottom padding for easier scrolling
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun AudioItem(
    audio: Audio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = audio.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = audio.category.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
