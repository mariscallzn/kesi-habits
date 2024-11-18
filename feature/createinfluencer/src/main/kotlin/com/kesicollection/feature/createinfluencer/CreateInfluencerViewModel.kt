package com.kesicollection.feature.createinfluencer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Influencer
import com.kesicollection.core.model.Status
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.influencer.InfluencerRepository
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
class CreateInfluencerViewModel @Inject constructor(
    private val influencerRepository: InfluencerRepository
) : ViewModel() {

    //region TextField state holders
    var influencerName by mutableStateOf("")
        private set

    fun updateInfluencerName(value: String) {
        influencerName = value
        dispatch(ScreenActions.ValidateFields)
    }
    //endregion

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<CreateInfluencerUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
                is ScreenActions.ClearText -> {
                    influencerName = ""
                    state.copy(isCreateEnable = false)
                }

                is ScreenActions.ValidateFields -> {
                    state.copy(isCreateEnable = influencerName.isNotBlank())
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
    val createInfluencer = createAsyncThunk<String, Unit>("create-influencer") { _, _ ->
        val uuid = "${UUID.randomUUID()}"
        influencerRepository.add(
            Influencer(
                id = uuid,
                name = influencerName,
                status = Status.ACTIVE
            )
        )
        uuid
    }.apply {
        store.builder.addCase(pending) { state, _ -> state.copy(isCreateEnable = false) }
        store.builder.addCase(fulfilled) { state, action ->
            action.payload.getOrNull()?.let { state.copy(createdInfluencerId = it) } ?: state
        }
    }
    //endregion
}