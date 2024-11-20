package com.kesicollection.feature.weeklyhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Day
import com.kesicollection.core.model.Week
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.weekspaging.WeekPagingSource
import com.kesicollection.data.weekspaging.model.PagingEvent
import com.kesicollection.domain.datetime.GetDayFromOffsetDateTime
import com.kesicollection.domain.datetime.GetDaysFromWeekUseCase
import com.kesicollection.domain.datetime.GetDisplayedDateFromOffsetDateTime
import com.kesicollection.domain.datetime.GetOffsetDateTimeFromIsoFormat
import com.kesicollection.domain.datetime.GetTimeFromOffsetDateTime
import com.kesicollection.feature.weeklyhabits.domain.MapEntryDays
import com.kesicollection.feature.weeklyhabits.model.EntryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

//region Screen Actions
sealed class ScreenAction {
    data object Reset : ScreenAction()
    data class LoadCalendar(
        val startFrom: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
        val locale: Locale
    ) :
        ScreenAction()

    data class SelectDay(val day: Day, val locale: Locale) : ScreenAction()

    data class HandlePagingEvent(val event: PagingEvent<Week>) : ScreenAction()
}
//endregion

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor(
    private val weekPagingSource: WeekPagingSource,
    private val mapEntryDays: MapEntryDays,
    private val getDaysFromWeekUseCase: GetDaysFromWeekUseCase,
    private val getDayFromOffsetDateTime: GetDayFromOffsetDateTime,
    private val getDisplayedDateFromOffsetDateTime: GetDisplayedDateFromOffsetDateTime,
    private val getOffsetDateTimeFromIsoFormat: GetOffsetDateTimeFromIsoFormat,
    private val getTimeFromOffsetDateTime: GetTimeFromOffsetDateTime
) : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<WeeklyHabitsUiState, ScreenAction> { state, action ->
            when (action) {
                is ScreenAction.Reset -> initialState
                is ScreenAction.LoadCalendar -> loadCalendar(state, action)
                is ScreenAction.HandlePagingEvent -> pagingEventHandler(
                    state,
                    action.event,
                    state.currentLocale
                )

                is ScreenAction.SelectDay -> state.copy(
                    selectedDay = action.day,
                    displayedDate = getDisplayedDateFromOffsetDateTime(
                        getOffsetDateTimeFromIsoFormat(action.day.rawUTCDateTime),
                        action.locale
                    )
                )
            }
        }
    )

    val uiState = store.subscribe.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = store.state
    )

    init {
        viewModelScope.launch {
            weekPagingSource.getPagingEvents().collect { pagingEvent ->
                dispatch(ScreenAction.HandlePagingEvent(pagingEvent))
            }
        }
    }

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    //region Reducer Handlers
    private fun loadCalendar(
        state: WeeklyHabitsUiState,
        action: ScreenAction.LoadCalendar
    ): WeeklyHabitsUiState {
        val pagingEvent = weekPagingSource.startFrom(
            action.startFrom.with(
                TemporalAdjusters.previousOrSame(
                    //TODO: Here we have to read the start day from settings repo
                    DayOfWeek.MONDAY
                )
            )
        )
        val day = getDayFromOffsetDateTime(
            action.startFrom,
            action.locale
        )
        return pagingEventHandler(
            state.copy(
                currentLocale = action.locale,
                currentDay = day,
                selectedDay = day,
                displayedDate = getDisplayedDateFromOffsetDateTime(action.startFrom, action.locale),
            ), pagingEvent, action.locale
        )
    }

    private fun pagingEventHandler(
        state: WeeklyHabitsUiState,
        event: PagingEvent<Week>, locale: Locale
    ): WeeklyHabitsUiState {
        return when (event) {
            is PagingEvent.Appended -> {
                dispatch(fetchEntriesByWeek(event.newData))
                state.copy(
                    offsetIndex = event.initialOffset,
                    weeks = state.weeks +
                            event.newData.map { week -> getDaysFromWeekUseCase(week, locale) }
                )
            }

            is PagingEvent.Initial -> state
            is PagingEvent.Prepended -> {
                dispatch(fetchEntriesByWeek(event.newData))
                state.copy(
                    offsetIndex = event.updatedComputedIndex,
                    weeks = event.newData.map { week ->
                        getDaysFromWeekUseCase(
                            week,
                            locale
                        )
                    } + state.weeks
                )
            }
        }
    }
    //endregion

    //region Async Thunks
    val watchPagingIndex = createAsyncThunk<Unit, Int>("watch-index") { args, _ ->
        weekPagingSource.watch(args)
    }

    val fetchEntriesByWeek =
        createAsyncThunk<Map<Day, List<EntryItem>>, List<Week>>("fetch-entries") { args, opt ->
            val state = opt.getState as WeeklyHabitsUiState
            mapEntryDays(args, state.currentLocale).mapValues { (_, entries) ->
                entries.map { entry ->
                    EntryItem(title = entry.habit?.name ?: "",
                        humanNeeds = entry.humanNeeds?.map { it.name }
                            ?: emptyList(),
                        time = getTimeFromOffsetDateTime(entry.recordedOn, state.currentLocale),
                        classification = entry.habit?.classification ?: Classification.NEUTRAL)
                }
            }
        }.apply {
            store.builder.addCase(fulfilled) { s, a ->
                a.payload.getOrNull()?.let { s.copy(entries = it) } ?: s
            }
        }
    //endregion

    override fun onCleared() {
        super.onCleared()
        weekPagingSource.clear()
    }
}