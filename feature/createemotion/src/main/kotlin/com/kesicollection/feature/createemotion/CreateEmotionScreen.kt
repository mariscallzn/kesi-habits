package com.kesicollection.feature.createemotion

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.CancelOutlinedTextField
import com.kesicollection.core.designsystem.component.CommonTopAppBar
import com.kesicollection.core.designsystem.component.CreationButton
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.core.model.Arousal
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Valence
import com.kesicollection.feature.createemotion.navigation.CreateEmotion
import com.kesicollection.feature.createemotion.navigation.EmotionId
import com.kesicollection.feature.createemotion.navigation.EntryDraftId

@Composable
fun CreateEmotionScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    createEmotion: CreateEmotion,
    onCreateEmotionClick: (EntryDraftId, EmotionId, EmotionType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateEmotionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d(TAG, "CreateEmotionScreen: $createEmotion")
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPressed)
        }

    }

    LaunchedEffect(uiState.createdEmotionId) {
        if (uiState.createdEmotionId.isNotBlank()) {
            onCreateEmotionClick(
                createEmotion.draftId,
                uiState.createdEmotionId,
                createEmotion.emotionType
            )
        }
    }

    CreateEmotionScreen(
        uiState = uiState,
        emotionName = viewModel.emotionName,
        onEmotionNameChangeValue = { value -> viewModel.updateEmotionName(value) },
        valence = viewModel.valence,
        onValenceChange = { v -> viewModel.updateValence(v) },
        arousal = viewModel.arousal,
        onArousalChange = { a -> viewModel.updateArousal(a) },
        onCancel = { viewModel.dispatch(ScreenActions.ClearText) },
        onCreate = { viewModel.dispatch(viewModel.createEmotion(Unit)) },
        modifier = modifier
    )
}

@Composable
fun CreateEmotionScreen(
    uiState: CreateEmotionUiState,
    emotionName: String,
    onEmotionNameChangeValue: (String) -> Unit,
    valence: Valence?,
    onValenceChange: (Valence) -> Unit,
    arousal: Arousal?,
    onArousalChange: (Arousal) -> Unit,
    onCancel: () -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        CancelOutlinedTextField(
            emotionName,
            onEmotionNameChangeValue,
            placeHolder = stringResource(R.string.new_emotion),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 120.dp, bottom = 16.dp),
            onCancel = onCancel
        )
        Text(
            text = stringResource(R.string.valence_description),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )
        ValenceChipsGroup(valence, onValenceChange)
        Text(
            text = stringResource(R.string.arousal_description),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )
        ArousalChipsGroup(arousal, onArousalChange)
        Box(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreationButton(
                onClick = onCreate,
                enabled = uiState.isCreateEnabled,
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(com.kesicollection.core.designsystem.R.string.create)
            )
        }
    }
}

@Composable
fun ValenceChipsGroup(
    valence: Valence?,
    onValenceChange: (Valence) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = valence == Valence.NEGATIVE,
            onClick = { onValenceChange(Valence.NEGATIVE) },
            label = { Text(stringResource(R.string.negative).uppercase()) },
            colors = FilterChipDefaults.filterChipColors()
                .copy(
                    selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.errorContainer
                ),
        )
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = valence == Valence.NEUTRAL,
            onClick = { onValenceChange(Valence.NEUTRAL) },
            label = { Text(stringResource(R.string.neutral).uppercase()) },
        )
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = valence == Valence.POSITIVE,
            onClick = { onValenceChange(Valence.POSITIVE) },
            label = { Text(stringResource(R.string.positive).uppercase()) },
            colors = FilterChipDefaults.filterChipColors()
                .copy(
                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
        )
    }
}

@Composable
fun ArousalChipsGroup(
    arousal: Arousal?,
    onArousalChange: (Arousal) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = arousal == Arousal.LOW,
            onClick = { onArousalChange(Arousal.LOW) },
            label = { Text(stringResource(R.string.low).uppercase()) },
            colors = FilterChipDefaults.filterChipColors()
                .copy(
                    selectedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
        )
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = arousal == Arousal.MODERATE,
            onClick = { onArousalChange(Arousal.MODERATE) },
            label = { Text(stringResource(R.string.moderate).uppercase()) },
            colors = FilterChipDefaults.filterChipColors()
                .copy(
                    selectedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
        )
        FilterChip(
            shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
            selected = arousal == Arousal.HIGH,
            onClick = { onArousalChange(Arousal.HIGH) },
            label = { Text(stringResource(R.string.high).uppercase()) },
            colors = FilterChipDefaults.filterChipColors()
                .copy(
                    selectedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
        )
    }
}