package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Emotion
import com.kesicollection.database.api.EmotionDb
import com.kesicollection.database.impl.room.dao.EmotionDao
import com.kesicollection.database.impl.room.model.toEmotion
import com.kesicollection.database.impl.room.model.toEntity
import javax.inject.Inject

class RoomEmotionDb @Inject constructor(
    private val emotionDao: EmotionDao
) : EmotionDb {
    override suspend fun insert(emotion: Emotion): Long = emotionDao.insert(emotion.toEntity())
    override suspend fun getAll(): List<Emotion> = emotionDao.getAll().map { it.toEmotion() }
}