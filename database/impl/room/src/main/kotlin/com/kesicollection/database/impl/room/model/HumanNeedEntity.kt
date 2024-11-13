package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.HumanNeed

open class Ranked(val ranked: Int = -1)

@Entity(tableName = "human_needs", ignoredColumns = ["ranked"])
data class HumanNeedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "i_18_key")
    val i18Key: String,
) : Ranked()

fun HumanNeedEntity.toHumanNeed(): HumanNeed = HumanNeed(id, name, i18Key, ranked)

fun HumanNeed.toEntity(): HumanNeedEntity = HumanNeedEntity(id, name, i18Key)