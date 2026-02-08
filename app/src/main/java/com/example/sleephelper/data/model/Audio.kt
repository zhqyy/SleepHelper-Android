package com.example.sleephelper.data.model

data class Audio(
    val id: String,
    val title: String,
    val category: AudioCategory,
    val url: String, // Stream URL or local path
    val coverResId: Int? = null, // Placeholder for resource ID if needed
    val duration: Long = 0L
)

enum class AudioCategory(val displayName: String) {
    WHITE_NOISE("白噪音"),
    STORY("睡前故事"),
    RECORDING("我的录音")
}
