package com.kesicollection.feature.weeklyhabits.domain

import com.kesicollection.core.model.Day
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.Week
import com.kesicollection.data.entry.EntryRepository
import com.kesicollection.domain.datetime.GetDaysFromWeekUseCase
import java.util.Locale
import javax.inject.Inject

/**
 * Maps a list of [Week] objects to a map of [Day] to [Entry] list.

 * This class takes a list of [Week] objects and a locale, extracts [Day] objects from each week,
 * and then retrieves the corresponding entries from the [EntryRepository] for each [Day].

 * @param entryRepository The repository for fetching entries.
 * @param getDaysFromWeekUseCase The use case for extracting [Day] objects from a [Week].
 */
class MapEntryDays @Inject constructor(
    private val entryRepository: EntryRepository,
    private val getDaysFromWeekUseCase: GetDaysFromWeekUseCase,
) {
    /**
     * Maps a list of [Week] objects to a map of [Day] to [Entry] list.

     * @param weeks The list of [Week] objects to process.
     * @param locale The locale to use for formatting the days of the week.

     * @return A map where the keys are [Day] objects and the values are lists of [Entry] objects
     * associated with that day.
     */
    suspend operator fun invoke(weeks: List<Week>, locale: Locale): Map<Day, List<Entry>> =
        weeks.flatMap { getDaysFromWeekUseCase(week = it, locale = locale) }
            .associateWith { entryRepository.getByDay(it) }
}