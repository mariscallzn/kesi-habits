package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.kesicollection.core.model.EntryHumanNeed

@Entity(
    tableName = "entries_human_needs",
    foreignKeys = [
        ForeignKey(
            entity = EntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["entry_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = HumanNeedEntity::class,
            parentColumns = ["id"],
            childColumns = ["human_need_id"],
            onDelete = CASCADE
        )
    ]
)
data class EntryHumanNeedCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "entry_id")
    val entryId: String,
    @ColumnInfo(name = "human_need_id")
    val humanNeedId: Int,
    val rank: Int
)

fun EntryHumanNeed.toEntity() = EntryHumanNeedCrossRef(id, entryId, humanNeedId, rank)
fun EntryHumanNeedCrossRef.toEntryHumanNeed() = EntryHumanNeed(id, entryId, humanNeedId, rank)