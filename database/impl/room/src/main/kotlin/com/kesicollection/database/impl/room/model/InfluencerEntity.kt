package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Influencer

@Entity(tableName = "influencers")
data class InfluencerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "i_18_key")
    val i18Key: String?
)

fun Influencer.toEntity(): InfluencerEntity = InfluencerEntity(
    id = id,
    name = name,
    i18Key = i18Key
)