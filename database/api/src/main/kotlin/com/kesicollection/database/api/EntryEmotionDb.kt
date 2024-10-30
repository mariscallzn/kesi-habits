package com.kesicollection.database.api

import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.EntryEmotion

interface EntryEmotionDb {
    suspend fun insert(entryEmotion: EntryEmotion, type: EmotionType): Long
}