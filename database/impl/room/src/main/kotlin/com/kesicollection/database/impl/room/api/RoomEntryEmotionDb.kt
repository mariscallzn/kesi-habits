package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.EntryEmotion
import com.kesicollection.database.api.EntryEmotionDb
import com.kesicollection.database.impl.room.dao.EntryEmotionDao
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toEntryEmotion
import javax.inject.Inject

class RoomEntryEmotionDb @Inject constructor(
    private val entryEmotionDao: EntryEmotionDao
) : EntryEmotionDb {

    override suspend fun getEntryEmotionByEntryIdAndType(
        entryId: String,
        type: EmotionType
    ): List<EntryEmotion> =
        entryEmotionDao.getEntryEmotionByEntryIdAndEmotionType(entryId, type)
            .map { it.toEntryEmotion() }


    override suspend fun delete(entryEmotionId: Long) {
        val entryEmotion = entryEmotionDao.findById(entryEmotionId)
        entryEmotionDao.delete(entryEmotion)
    }

    override suspend fun insert(entryEmotion: EntryEmotion): Long = entryEmotionDao.insert(
        entryEmotion.toEntity()
    )

}