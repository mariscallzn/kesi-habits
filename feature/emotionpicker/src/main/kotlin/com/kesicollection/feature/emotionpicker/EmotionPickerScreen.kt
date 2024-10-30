package com.kesicollection.feature.emotionpicker

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.CommonTopAppBar
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.EmotionType
import com.kesicollection.feature.emotionpicker.navigation.EmotionPicker
import com.kesicollection.feature.emotionpicker.navigation.EntryDraftId

@Composable
fun EmotionPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    emotionPicker: EmotionPicker,
    onCreateEmotionClick: (EntryDraftId, EmotionType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmotionPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPress, "Emotion picker")
        }
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(onClick = {
                onCreateEmotionClick(emotionPicker.entryDraftId, emotionPicker.emotionType)
            }) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }

    EmotionPickerScreen(uiState, modifier = modifier)
}

@Composable
fun EmotionPickerScreen(uiState: EmotionPickerUiState, modifier: Modifier = Modifier) {

}