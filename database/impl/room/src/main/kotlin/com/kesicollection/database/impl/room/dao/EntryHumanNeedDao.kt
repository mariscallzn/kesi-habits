package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.EntryHumanNeedCrossRef

@Dao
interface EntryHumanNeedDao : BaseDao<EntryHumanNeedCrossRef> {

    @Query(
        """
        SELECT * FROM entries_human_needs
        WHERE id = :id
    """
    )
    suspend fun findById(id: Long): EntryHumanNeedCrossRef

    @Query(
        """
        SELECT * FROM entries_human_needs
        WHERE entry_id = :entryId
    """
    )
    suspend fun getHumanNeedsByEntryId(entryId: String): List<EntryHumanNeedCrossRef>
}