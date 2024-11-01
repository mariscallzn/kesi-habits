package com.kesicollection.feature.emotionpicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Emotion
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.emotion.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data class SelectionChange(val selectedId: String) : ScreenActions()
    data class Filter(val searchTerm: String) : ScreenActions()
    data object ClearTextField : ScreenActions()
    data class SelectItems(val ids: List<String>) : ScreenActions()
}
//endregion

@HiltViewModel
class EmotionPickerViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository
) : ViewModel() {

    //region TextField state holder
    var searchTerm by mutableStateOf("")
        private set

    fun updateSearchTerm(value: String) {
        searchTerm = value
        dispatch(ScreenActions.Filter(value))
    }
    //endregion

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<EmotionPickerUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState

                is ScreenActions.SelectionChange -> {
                    val mutableSet = state.selectedItems.toMutableSet()
                    val isSelected = state.selectedItems.contains(action.selectedId)
                    if (isSelected) mutableSet -= action.selectedId else mutableSet += action.selectedId
                    state.copy(selectedItems = mutableSet)
                }

                is ScreenActions.Filter -> filter(state, action.searchTerm)

                is ScreenActions.ClearTextField -> {
                    searchTerm = ""
                    filter(state, searchTerm)
                }

                is ScreenActions.SelectItems -> state.copy(selectedItems = state.selectedItems + action.ids.toSet())
            }
        }
    )

    val uiState = store.subscribe.onStart {
        dispatch(loadEmotions(Unit))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialState
    )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    private fun filter(state: EmotionPickerUiState, term: String): EmotionPickerUiState {
        val filtered = state.cacheEmotions.filter { it.name.contains(term, ignoreCase = true) }
        return if (term.isNotBlank()) state.copy(emotions = filtered) else
            state.copy(emotions = state.cacheEmotions)
    }

    //region Async Thunks
    val loadEmotions = createAsyncThunk<List<Emotion>, Unit>("") { _, _ ->
        emotionRepository.fetch()
    }.apply {
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()
                ?.let { s.copy(emotions = it, cacheEmotions = it, selectedItems = emptySet()) } ?: s
        }
    }
    //endregion
}