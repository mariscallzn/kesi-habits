package com.kesicollection.feature.createemotion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Arousal
import com.kesicollection.core.model.Emotion
import com.kesicollection.core.model.Status
import com.kesicollection.core.model.Valence
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.emotion.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data object ValidateFields : ScreenActions()
    data object ClearText : ScreenActions()
}
//endregion

@HiltViewModel
class CreateEmotionViewModel @Inject constructor(
    private val emotionRepository: EmotionRepository
) : ViewModel() {

    //region TextField state holders
    var emotionName by mutableStateOf("")
        private set

    var valence by mutableStateOf<Valence?>(null)
        private set

    var arousal by mutableStateOf<Arousal?>(null)
        private set

    fun updateEmotionName(value: String) {
        emotionName = value
        dispatch(ScreenActions.ValidateFields)
    }

    fun updateValence(updatedValence: Valence) {
        valence = updatedValence
        dispatch(ScreenActions.ValidateFields)
    }

    fun updateArousal(updatedArousal: Arousal) {
        arousal = updatedArousal
        dispatch(ScreenActions.ValidateFields)
    }
    //endregion

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<CreateEmotionUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
                ScreenActions.ValidateFields ->
                    state.copy(
                        isCreateEnabled = emotionName.isNotBlank() &&
                                valence != null &&
                                arousal != null
                    )

                ScreenActions.ClearText -> {
                    emotionName = ""
                    valence = null
                    arousal = null
                    state.copy(isCreateEnabled = false)
                }
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
    val createEmotion = createAsyncThunk<String, Unit>("create-emotion") { _, _ ->
        val uuid = "${UUID.randomUUID()}"
        emotionRepository.add(
            Emotion(
                id = uuid,
                name = emotionName,
                valence = valence ?: Valence.NEUTRAL,
                arousal = arousal ?: Arousal.LOW,
                status = Status.ACTIVE
            )
        )
        uuid
    }.apply {
        store.builder.addCase(pending) { state, _ -> state.copy(isCreateEnabled = false) }
        store.builder.addCase(fulfilled) { state, action ->
            action.payload.getOrNull()?.let { state.copy(createdEmotionId = it) } ?: state
        }
        store.builder.addCase(rejected) { state, _ -> state }
    }
    //endregion
}