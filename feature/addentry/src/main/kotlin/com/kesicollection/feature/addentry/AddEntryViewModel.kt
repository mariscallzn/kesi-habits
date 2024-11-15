package com.kesicollection.feature.addentry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HumanNeed
import com.kesicollection.core.model.Status
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.entry.EntryRepository
import com.kesicollection.data.humanneed.HumanNeedRepository
import com.kesicollection.domain.datetime.GetDateFromMillis
import com.kesicollection.domain.datetime.GetDateFromOffsetDateTime
import com.kesicollection.domain.datetime.GetFormattedOffsetDateTime
import com.kesicollection.domain.datetime.GetTimeFromOffsetDateTime
import com.kesicollection.domain.datetime.GetTimeFromTimePicker
import com.kesicollection.domain.datetime.GetTimePairFromOffsetDateTime
import com.kesicollection.feature.addentry.domain.SortHumanNeeds
import com.kesicollection.feature.addentry.model.CreateDraftThunk
import com.kesicollection.feature.addentry.model.UpdateTimeThunk
import com.kesicollection.feature.addentry.navigation.AddEntry
import com.kesicollection.feature.addentry.navigation.EntryDraftId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.time.OffsetDateTime
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data class DefineLocale(val locale: Locale) : ScreenActions()
    data class ShowTimeDialog(val isShowing: Boolean) : ScreenActions()
    data class ShowInitialDateTime(
        val offsetDateTime: OffsetDateTime,
        val locale: Locale
    ) :
        ScreenActions()

    data class ShowDateDialog(val isShowing: Boolean) : ScreenActions()
    data class DateSelected(val millis: Long?, val locale: Locale) : ScreenActions()
    data class TimeSelected(
        val hour: Int,
        val minute: Int, val isAfternoon: Boolean
    ) :
        ScreenActions()
}

//endregion
@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val humanNeedRepository: HumanNeedRepository,
    private val getDateFromMillis: GetDateFromMillis,
    private val getTimeFromOffsetDateTime: GetTimeFromOffsetDateTime,
    private val getTimeFromTimePicker: GetTimeFromTimePicker,
    private val getDateFromOffsetDateTime: GetDateFromOffsetDateTime,
    private val getTimePairFromOffsetDateTime: GetTimePairFromOffsetDateTime,
    private val getFormattedOffsetDateTime: GetFormattedOffsetDateTime,
    private val sortHumanNeeds: SortHumanNeeds,
) : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<AddEntryUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
                is ScreenActions.DefineLocale -> state.copy(locale = action.locale)
                is ScreenActions.ShowDateDialog -> state.copy(isDateShowing = action.isShowing)
                is ScreenActions.ShowTimeDialog -> state.copy(isTimeShowing = action.isShowing)

                is ScreenActions.DateSelected -> state.copy(
                    isDateShowing = false,
                    formattedDate = getDateFromMillis(action.millis, action.locale)
                )

                is ScreenActions.TimeSelected -> state.copy(
                    isTimeShowing = false,
                    time = action.hour to action.minute,
                    formattedTime = getTimeFromTimePicker(
                        action.hour,
                        action.minute,
                        action.isAfternoon
                    )
                )

                is ScreenActions.ShowInitialDateTime -> state.copy(
                    formattedTime = getTimeFromOffsetDateTime(
                        action.offsetDateTime,
                        action.locale
                    ),
                    formattedDate = getDateFromOffsetDateTime(action.offsetDateTime, action.locale),
                    time = getTimePairFromOffsetDateTime(action.offsetDateTime)
                )
            }
        }
    )

    val uiState = store.subscribe.onStart {
        dispatch(loadHumanNeeds(Unit))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialState
    )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    //region Async Thunks
    val loadDraft = createAsyncThunk<Entry, String>("load-draft") { entryId, _ ->
        entryRepository.getById(entryId)
    }.apply {
        store.builder.addCase(pending) { s, _ -> s }
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()?.let {
                s.copy(
                    draftId = it.id,
                    coreHabit = it.habit,
                    triggerHabit = it.triggeredBy,
                    influencers = it.influencers ?: emptyList(),
                    formattedDate = getDateFromOffsetDateTime(it.recordedOn, s.locale),
                    formattedTime = getTimeFromOffsetDateTime(it.recordedOn, s.locale),
                    time = getTimePairFromOffsetDateTime(it.recordedOn),
                    humanNeeds = it.humanNeeds ?: s.humanNeeds,
                    isSaveEnabled = it.habit != null,
                )
            } ?: s
        }
        store.builder.addCase(rejected) { s, _ -> s }
    }

    val createDraft = createAsyncThunk<EntryDraftId, CreateDraftThunk>("draft") { args, options ->
        val draftId = "${UUID.randomUUID()}"
        options.dispatch(
            ScreenActions.ShowInitialDateTime(
                args.offsetDateTime,
                args.locale
            )
        )
        entryRepository.add(Entry(draftId, args.offsetDateTime, Status.DRAFT))
        draftId
    }.apply {
        store.builder.addCase(fulfilled) { state, action ->
            action.payload.getOrNull()
                ?.let { state.copy(draftId = it) } ?: state
        }
        store.builder.addCase(rejected) { s, _ ->
            s.copy(isSaveEnabled = false, draftId = "")
        }
    }

    val updateTime = createAsyncThunk<Unit, UpdateTimeThunk>("update-time") { args, opt ->
        val state = opt.getState as AddEntryUiState
        entryRepository.updateTime(state.draftId, args.hour, args.minute)
        opt.dispatch(loadDraft(state.draftId))
    }

    val updateDate = createAsyncThunk<Unit, Long?>("update-date") { args, opt ->
        val state = opt.getState as AddEntryUiState
        entryRepository.updateDate(state.draftId, args)
        opt.dispatch(loadDraft(state.draftId))
    }

    val updateHabit =
        createAsyncThunk<Unit, AddEntry>("update-habit") { args, options ->
            args.draftId?.let {
                entryRepository.updateHabit(
                    entryId = args.draftId,
                    habitId = args.habitId,
                    habitType = args.habitType,
                    influencersIds = args.influencerIds
                )
                options.dispatch(loadDraft(args.draftId))
            } ?: throw IllegalStateException("Entry Id must not be null")
        }.apply {
            store.builder.addCase(rejected) { state, action ->
                Log.e(TAG, "updateHabit:rejected ${action.payload.exceptionOrNull()}")
                state.copy(isSaveEnabled = false)
            }
        }

    val updateHumanNeed = createAsyncThunk<Unit, HumanNeed>("update-human-need") { args, options ->
        val state = options.getState as AddEntryUiState
        entryRepository.updateHumanNeeds(state.draftId, sortHumanNeeds(args, state.humanNeeds))
        options.dispatch(loadDraft(state.draftId))
    }

    val draftFinished = createAsyncThunk<OffsetDateTime, Unit>("draft-finished") { _, options ->
        val state = options.getState as AddEntryUiState
        entryRepository.updateEntryStatus(state.draftId, Status.ACTIVE).recordedOn
    }.apply {
        store.builder.addCase(pending) { s, _ -> s.copy(isSaveEnabled = false) }
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()?.let { s.copy(recordedOn = getFormattedOffsetDateTime(it)) } ?: s
        }
        store.builder.addCase(rejected) { s, _ ->
            s.copy(isSaveEnabled = false)
        }
    }

    val loadHumanNeeds = createAsyncThunk<List<HumanNeed>, Unit>("load-human-needs") { _, _ ->
        humanNeedRepository.fetch()
    }.apply {
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()?.let { s.copy(humanNeeds = it) } ?: s
        }
    }
    //endregion


}