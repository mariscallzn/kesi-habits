package com.kesicollection.feature.addentry.domain

import com.kesicollection.core.model.HumanNeed
import javax.inject.Inject

class SortHumanNeeds @Inject constructor() {

    /**
     * Updates the ranking of a [HumanNeed] within a collection.
     *
     * If the incoming [HumanNeed] has a rank of -1, it is assigned the highest rank + 1.
     * If the incoming [HumanNeed] has a rank other than -1, it is set to -1, and the remaining
     * HumanNeeds are re-ranked sequentially.
     *
     * @param incoming The [HumanNeed] to update the ranking for.
     * @param collection The collection of HumanNeeds.
     * @return The updated collection of HumanNeeds.
     */
    operator fun invoke(incoming: HumanNeed, collection: List<HumanNeed>): List<HumanNeed> {
        return if (incoming.ranked == -1) {
            val highestRanked = collection.maxOfOrNull { it.ranked } ?: -1
            val newRank = if (highestRanked == -1) 1 else highestRanked + 1
            collection.map {
                if (it.id == incoming.id) it.copy(ranked = newRank) else it
            }
        } else {
            val sortedList = collection.filter { it.ranked != -1 && it.id != incoming.id }
                .sortedBy { it.ranked } // Sort for ranking update

            val updatedList = sortedList.mapIndexed { index, need ->
                need.copy(ranked = index + 1)
            }

            collection.map {
                it.copy(
                    ranked = updatedList.find { i -> i.id == it.id }?.ranked ?: -1
                )
            }
        }
    }
}