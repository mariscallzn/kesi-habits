package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Influencer
import com.kesicollection.core.model.Status

@Entity(tableName = "influencers")
data class InfluencerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val status: Status,
    @ColumnInfo(name = "i_18_key")
    val i18Key: String?
)

fun Influencer.toEntity(): InfluencerEntity = InfluencerEntity(
    id = id,
    name = name,
    i18Key = i18Key,
    status = status
)

fun InfluencerEntity.toInfluencer(): Influencer = Influencer(id, name, status, i18Key)