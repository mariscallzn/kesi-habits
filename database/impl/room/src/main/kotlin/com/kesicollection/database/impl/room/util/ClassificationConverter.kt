package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import com.kesicollection.core.model.Classification

internal class ClassificationConverter {
    @TypeConverter
    fun toClassification(value: String?): Classification =
        Classification.valueOf(value ?: "NEUTRAL")


    @TypeConverter
    fun fromClassification(classification: Classification?): String? =
        classification?.name

}