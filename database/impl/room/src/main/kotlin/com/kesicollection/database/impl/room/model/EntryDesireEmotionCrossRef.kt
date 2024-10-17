package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "entries_desire_emotions",
    primaryKeys = ["entry_id", "emotion_id"],
    foreignKeys = [
        ForeignKey(
            entity = EntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entry_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = EmotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["emotion_id"],
            onDelete = CASCADE
        ),
    ]
)
data class EntryDesireEmotionCrossRef(
    @ColumnInfo(name = "entry_id")
    val entryId: String,
    @ColumnInfo(name = "emotion_id")
    val emotionId: String
)