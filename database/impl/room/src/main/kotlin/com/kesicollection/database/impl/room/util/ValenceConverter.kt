package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import com.kesicollection.core.model.Valence

internal class ValenceConverter {

    @TypeConverter
    fun toValence(value: String?): Valence = Valence.valueOf(value?: "NEUTRAL")

    @TypeConverter
    fun fromValence(valence: Valence?): String? = valence?.name
}