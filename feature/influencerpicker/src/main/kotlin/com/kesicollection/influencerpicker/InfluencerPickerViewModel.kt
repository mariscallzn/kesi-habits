package com.kesicollection.influencerpicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Influencer
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.influencer.InfluencerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data class Filter(val searchTerm: String) : ScreenActions()
    data object ClearTextField : ScreenActions()
    data class SelectItems(val ids: List<String>) : ScreenActions()
    data class SelectionChange(val selectedId: String) : ScreenActions()
}
//endregion

@HiltViewModel
class InfluencerPickerViewModel @Inject constructor(
    private val influencerRepository: InfluencerRepository
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
        reducer = reducer<InfluencerPickerUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
                is ScreenActions.Filter -> filter(state, action.searchTerm)
                is ScreenActions.SelectionChange -> {
                    val mutableSet = state.selectedItems.toMutableSet()
                    val isSelected = state.selectedItems.contains(action.selectedId)
                    if (isSelected) mutableSet -= action.selectedId else mutableSet += action.selectedId
                    state.copy(selectedItems = mutableSet)
                }

                is ScreenActions.ClearTextField -> {
                    searchTerm = ""
                    filter(state, searchTerm)
                }

                is ScreenActions.SelectItems -> state.copy(
                    isEditing = true,
                    selectedItems = state.selectedItems + action.ids.toSet()
                )
            }
        }
    )

    val uiState = store.subscribe.onStart { dispatch(loadInfluencers(Unit)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState
        )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    private fun filter(state: InfluencerPickerUiState, term: String): InfluencerPickerUiState {
        return if (term.isNotBlank()) state.copy(displayedInfluencers = state.cacheInfluencers.filter {
            it.name.contains(
                term,
                ignoreCase = true
            )
        }) else state.copy(displayedInfluencers = state.cacheInfluencers)
    }

    //region Async Thunks
    val loadInfluencers = createAsyncThunk<List<Influencer>, Unit>("load-influences") { _, _ ->
        influencerRepository.fetch()
    }.apply {
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()?.let { s.copy(cacheInfluencers = it, displayedInfluencers = it) }
                ?: s
        }
    }
    //endregion
}