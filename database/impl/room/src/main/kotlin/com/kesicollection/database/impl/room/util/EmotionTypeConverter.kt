package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import com.kesicollection.core.model.EmotionType

internal class EmotionTypeConverter {

    @TypeConverter
    fun toEmotionType(value: String?): EmotionType = EmotionType.valueOf(value?: "CURRENT")

    @TypeConverter
    fun fromEmotionType(emotionType: EmotionType?): String? = emotionType?.name
}