package com.kesicollection.database.api

import com.kesicollection.core.model.Emotion

interface EmotionDb {
    suspend fun insert(emotion: Emotion): Long
}