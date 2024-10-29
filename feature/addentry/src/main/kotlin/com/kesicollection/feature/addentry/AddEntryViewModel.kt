package com.kesicollection.feature.addentry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.Status
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.entry.EntryRepository
import com.kesicollection.feature.addentry.model.UpdateHabitThunkArgs
import com.kesicollection.feature.addentry.navigation.EntryDraftId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
}

//endregion
@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository
) : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<AddEntryUiState, ScreenActions> { _, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
            }
        }
    )

    val uiState = store.subscribe.stateIn(
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
                    triggerHabit = it.triggeredBy
                )
            } ?: s
        }
        store.builder.addCase(rejected) { s, _ -> s }
    }

    val createDraft = createAsyncThunk<EntryDraftId, Unit>("draft") { _, _ ->
        val draftId = "${UUID.randomUUID()}"
        entryRepository.add(Entry(draftId, OffsetDateTime.now(ZoneOffset.UTC), Status.DRAFT))
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

    val updateHabit =
        createAsyncThunk<Unit, UpdateHabitThunkArgs>("update-habit") { args, options ->
            entryRepository.updateHabit(
                args.entryDraftId,
                args.habitId,
                args.habitType
            )

            options.dispatch(loadDraft(args.entryDraftId))
        }.apply {
            store.builder.addCase(rejected) { state, action ->
                Log.e(TAG, "updateHabit:rejected ${action.payload.exceptionOrNull()}")
                state.copy(isSaveEnabled = false)
            }
        }
    //endregion


}