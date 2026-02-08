package com.example.sleephelper.data.repository

import android.content.Context
import com.example.sleephelper.data.model.Audio
import com.example.sleephelper.data.model.AudioCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Mock Data for MVP
    private val mockAudios = listOf(
        Audio(
            id = "1",
            title = "绵绵细雨",
            category = AudioCategory.WHITE_NOISE,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),
        Audio(
            id = "2",
            title = "海浪声",
            category = AudioCategory.WHITE_NOISE,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        ),
        Audio(
            id = "3",
            title = "运转的风扇",
            category = AudioCategory.WHITE_NOISE,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        ),
        Audio(
            id = "4",
            title = "小红帽",
            category = AudioCategory.STORY,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
        ),
        Audio(
            id = "5",
            title = "丑小鸭",
            category = AudioCategory.STORY,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
        )
    )

    fun getAudiosByCategory(category: AudioCategory): Flow<List<Audio>> = flow {
        if (category == AudioCategory.RECORDING) {
            emit(getLocalRecordings())
        } else {
            emit(mockAudios.filter { it.category == category })
        }
    }

    fun getAllAudios(): Flow<List<Audio>> = flow {
        val all = mockAudios + getLocalRecordings()
        emit(all)
    }

    fun getAudioById(id: String): Audio? {
        // First check mock data
        val mock = mockAudios.find { it.id == id }
        if (mock != null) return mock

        // Then check local recordings
        return getLocalRecordings().find { it.id == id }
    }

    private fun getLocalRecordings(): List<Audio> {
        val directory = File(context.filesDir, "recordings")
        if (!directory.exists()) return emptyList()

        return directory.listFiles()
            ?.filter { it.extension == "mp3" }
            ?.map { file ->
                Audio(
                    id = file.name, // Use filename as ID for simplicity
                    title = file.nameWithoutExtension.substringAfter("REC_").substringBeforeLast("_"),
                    category = AudioCategory.RECORDING,
                    url = file.absolutePath
                )
            } ?: emptyList()
    }
}
