package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.EntryEmotion

@Entity(
    tableName = "entries_emotions",
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
data class EntryEmotionCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "entry_id")
    val entryId: String,
    @ColumnInfo(name = "emotion_id")
    val emotionId: String,
    @ColumnInfo(name = "emotion_type")
    val emotionType: EmotionType,
)

fun EntryEmotion.toEntity(): EntryEmotionCrossRef = EntryEmotionCrossRef(
    entryId = entryId, emotionId = emotionId, emotionType = emotionType
)

fun EntryEmotionCrossRef.toEntryEmotion(): EntryEmotion =
    EntryEmotion(id, entryId, emotionId, emotionType)