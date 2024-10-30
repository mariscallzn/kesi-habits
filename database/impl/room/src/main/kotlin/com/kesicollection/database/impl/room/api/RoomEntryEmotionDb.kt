package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.EntryEmotion
import com.kesicollection.database.api.EntryEmotionDb
import com.kesicollection.database.impl.room.dao.EntryCurrentEmotionDao
import com.kesicollection.database.impl.room.dao.EntryDesireEmotionDao
import com.kesicollection.database.impl.room.model.EntryCurrentEmotionCrossRef
import com.kesicollection.database.impl.room.model.EntryDesireEmotionCrossRef
import javax.inject.Inject

class RoomEntryEmotionDb @Inject constructor(
    private val currentEmotionDao: EntryCurrentEmotionDao,
    private val desireEmotionDao: EntryDesireEmotionDao
) : EntryEmotionDb {
    override suspend fun insert(entryEmotion: EntryEmotion, type: EmotionType): Long = when (type) {
        EmotionType.CURRENT -> currentEmotionDao.insert(
            EntryCurrentEmotionCrossRef(
                entryEmotion.entryId,
                entryEmotion.emotionId,
            )
        )

        EmotionType.DESIRE -> desireEmotionDao.insert(
            EntryDesireEmotionCrossRef(
                entryEmotion.entryId,
                entryEmotion.emotionId,
            )
        )
    }

}