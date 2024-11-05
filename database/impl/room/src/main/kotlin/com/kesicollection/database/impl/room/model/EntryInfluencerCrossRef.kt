package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.kesicollection.core.model.EntryInfluencer

@Entity(
    tableName = "entries_influencers",
    foreignKeys = [
        ForeignKey(
            entity = EntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entry_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = InfluencerEntity::class,
            parentColumns = ["id"],
            childColumns = ["influencer_id"],
            onDelete = CASCADE
        )
    ]
)
data class EntryInfluencerCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "entry_id")
    val entryId: String,
    @ColumnInfo(name = "influencer_id")
    val influencerId: String
)

fun EntryInfluencer.toEntity(): EntryInfluencerCrossRef = EntryInfluencerCrossRef(
    entryId = entryId, influencerId = influencerId
)

fun EntryInfluencerCrossRef.toEntryInfluencer(): EntryInfluencer =
    EntryInfluencer(id, entryId, influencerId)