package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "entries_influencers",
    primaryKeys = ["entry_id", "influencer_id"],
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
    @ColumnInfo(name = "entry_id")
    val entryId: String,
    @ColumnInfo(name = "influencer_id")
    val influencerId: String
)