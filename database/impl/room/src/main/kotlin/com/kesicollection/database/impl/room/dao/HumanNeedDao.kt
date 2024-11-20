package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.HumanNeedEntity

@Dao
interface HumanNeedDao : BaseDao<HumanNeedEntity> {

    @Query("SELECT * FROM human_needs")
    suspend fun getAll(): List<HumanNeedEntity>

    @Query("""
        SELECT * FROM human_needs
        WHERE id = :id
    """)
    suspend fun findById(id: Int): HumanNeedEntity

    @Query(
        """
            SELECT hn.id, hn.name, hn.i_18_key, ehn.rank as ranked FROM human_needs hn
            INNER JOIN entries_human_needs ehn ON hn.id = ehn.human_need_id
            WHERE ehn.entry_id = :entryId AND ranked != -1
        """
    )
    suspend fun getHumanNeedsByEntryId(entryId: String): List<HumanNeedEntity>
}