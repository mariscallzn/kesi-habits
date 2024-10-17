package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import com.kesicollection.core.model.Arousal

internal class ArousalConverter {

    @TypeConverter
    fun toArousal(value: String?): Arousal = Arousal.valueOf(value ?: "MODERATE")

    @TypeConverter
    fun fromArousal(arousal: Arousal?): String? = arousal?.name
}