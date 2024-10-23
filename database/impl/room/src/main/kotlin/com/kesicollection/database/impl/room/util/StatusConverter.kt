package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import com.kesicollection.core.model.Status

internal class StatusConverter {

    @TypeConverter
    fun toStatus(value: String?): Status = Status.valueOf(value ?: "DRAFT")

    @TypeConverter
    fun fromStatus(status: Status?): String? = status?.name
}