package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Influencer
import com.kesicollection.core.model.Status
import com.kesicollection.database.api.InfluencerDb
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.InfluencerEntity
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toInfluencer
import javax.inject.Inject

class RoomInfluencerDb @Inject constructor(
    private val influencerDao: InfluencerDao
) : InfluencerDb {
    override suspend fun insert(influencer: Influencer): Long =
        influencerDao.insert(influencer.toEntity())

    override suspend fun getAll(): List<Influencer> {
        val influencers = influencerDao.getAll()
        return if (influencers.isEmpty()) {
            influencerDao.insertAll(
                listOf(
                    InfluencerEntity(
                        "5ed78306-973a-41e1-84f7-d4a92a971784", "Peer pressure",
                        Status.ACTIVE,
                        "peer_pressure"
                    ),
                    InfluencerEntity(
                        "76aa9105-c997-4124-8c95-4f9408a64bc8",
                        "Social media",
                        Status.ACTIVE,
                        "social_media"
                    ),
                    InfluencerEntity(
                        "6a2741ca-d075-4a3d-af80-e5f087b315a1",
                        "Reciprocity",
                        Status.ACTIVE,
                        "reciprocity"
                    ),
                    InfluencerEntity(
                        "37ebfcba-f537-4b30-beec-818b6613312e",
                        "Scarcity",
                        Status.ACTIVE,
                        "scarcity"
                    ),
                    InfluencerEntity(
                        "61958534-46ae-4b8e-8c13-a43c964d8cd3",
                        "Peer group",
                        Status.ACTIVE,
                        "peer_group"
                    ),
                    InfluencerEntity(
                        "d686fafb-321b-41d2-92c7-1397dd37b8e2",
                        "Work",
                        Status.ACTIVE,
                        "work"
                    ),
                    InfluencerEntity(
                        "5124a19a-dfe9-4e6f-b6dc-e3d577d2ef11",
                        "Family",
                        Status.ACTIVE,
                        "family"
                    ),
                    InfluencerEntity(
                        "7b34d3d3-db68-49c2-b3f6-277b1f19b7ec",
                        "Friends",
                        Status.ACTIVE,
                        "friends"
                    ),
                    InfluencerEntity(
                        "bf4cad63-625b-4111-b3ec-879921376a9a",
                        "Hobbies",
                        Status.ACTIVE,
                        "hobbies"
                    ),
                    InfluencerEntity(
                        "7260dff6-e4ab-4c90-a7b0-90b67628b167",
                        "Emotional state",
                        Status.ACTIVE,
                        "emotional_state"
                    ),
                    InfluencerEntity(
                        "5f652c1e-ed03-4026-81f2-df7b22f98cff",
                        "Time",
                        Status.ACTIVE,
                        "time"
                    ),
                    InfluencerEntity(
                        "55276906-edf4-4765-a145-61cf73799297",
                        "Location",
                        Status.ACTIVE,
                        "location"
                    ),
                    InfluencerEntity(
                        "c90cbe73-2369-40f5-b16d-5f73c597e68d",
                        "Reward",
                        Status.ACTIVE,
                        "reward"
                    ),
                )
            )
            influencerDao.getAll().map { it.toInfluencer() }
        } else influencers.map { it.toInfluencer() }
    }
}