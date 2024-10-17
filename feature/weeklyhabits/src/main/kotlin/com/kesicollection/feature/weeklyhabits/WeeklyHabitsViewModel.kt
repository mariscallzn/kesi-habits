package com.kesicollection.feature.weeklyhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Week
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.habit.HabitRepository
import com.kesicollection.data.weekspaging.WeekPagingSource
import com.kesicollection.data.weekspaging.model.PagingEvent
import com.kesicollection.domain.weeklyhabits.GetDaysFromWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

//region Screen Actions
sealed class ScreenAction {
    data object Reset : ScreenAction()
    data class LoadCalendar(val startFrom: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)) :
        ScreenAction()

    data class HandlePagingEvent(val event: PagingEvent<Week>) : ScreenAction()
}
//endregion

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val weekPagingSource: WeekPagingSource,
    private val getDaysFromWeekUseCase: GetDaysFromWeekUseCase,
) : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<WeeklyHabitsUiState, ScreenAction> { state, action ->
            when (action) {
                is ScreenAction.Reset -> initialState
                is ScreenAction.LoadCalendar -> loadCalendar(state, action)
                is ScreenAction.HandlePagingEvent -> pagingEventHandler(state, action.event)
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
        //TODO: Here we have to read the start day from settings repo
        val pagingEvent = weekPagingSource.startFrom(
            action.startFrom.with(
                TemporalAdjusters.previousOrSame(
                    DayOfWeek.MONDAY
                )
            )
        )

        return pagingEventHandler(state, pagingEvent)
    }

    private fun pagingEventHandler(
        state: WeeklyHabitsUiState,
        event: PagingEvent<Week>
    ): WeeklyHabitsUiState {
        return when (event) {
            is PagingEvent.Appended -> {
                //TODO: Dispatch data fetching from DB
                state.copy(
                    offsetIndex = event.initialOffset,
                    weeks = state.weeks +
                            event.newData.map { week -> getDaysFromWeekUseCase(week) }
                )
            }

            is PagingEvent.Initial -> state
            is PagingEvent.Prepended -> {
                //TODO: Dispatch data fetching from DB
                state.copy(
                    offsetIndex = event.updatedComputedIndex,
                    weeks = event.newData.map { week -> getDaysFromWeekUseCase(week) } + state.weeks
                )
            }
        }
    }
    //endregion

    //region Async Thunks
    val watchPagingIndex = createAsyncThunk<Unit, Int>("watch-index") { args, _ ->
        weekPagingSource.watch(args)
    }
    //endregion

    override fun onCleared() {
        super.onCleared()
        weekPagingSource.clear()
    }
}