package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.EmotionEntity

@Dao
abstract class EmotionDao: BaseDao<EmotionEntity> {

    @Query("SELECT * FROM emotions")
    abstract suspend fun getAll(): List<EmotionEntity>
}