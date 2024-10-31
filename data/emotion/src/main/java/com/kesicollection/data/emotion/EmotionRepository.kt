package com.kesicollection.data.emotion

import com.kesicollection.core.model.Emotion
import com.kesicollection.database.api.EmotionDb
import javax.inject.Inject

interface EmotionRepository {
    suspend fun add(emotion: Emotion): Long
    suspend fun fetch(): List<Emotion>
}

class EmotionRepositoryImpl @Inject constructor(
    private val emotionDb: EmotionDb
) : EmotionRepository {
    override suspend fun add(emotion: Emotion): Long = emotionDb.insert(emotion)
    override suspend fun fetch(): List<Emotion> = emotionDb.getAll()
}