package com.kesicollection.feature.emotionpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
}
//endregion

@HiltViewModel
class EmotionPickerViewModel @Inject constructor() : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<EmotionPickerUiState, ScreenActions> {state, action ->
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
    //endregion
}