package com.kesicollection.feature.emotionpicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.SearchTopBar
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.utils.statusBarPaddingValues
import com.kesicollection.core.model.Emotion
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Valence
import com.kesicollection.feature.emotionpicker.navigation.EmotionId
import com.kesicollection.feature.emotionpicker.navigation.EmotionPicker
import com.kesicollection.feature.emotionpicker.navigation.EntryDraftId

@Composable
fun EmotionPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    emotionPicker: EmotionPicker,
    onCreateEmotionClick: (EntryDraftId, EmotionType) -> Unit,
    onEmotionsSelected: (EntryDraftId, List<EmotionId>, EmotionType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmotionPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineAppBarComposable {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = statusBarPaddingValues().calculateTopPadding(),
                        bottom = 0.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {

                SearchTopBar(
                    value = viewModel.searchTerm,
                    onValueChange = { v -> viewModel.updateSearchTerm(v) },
                    onCancel = { viewModel.dispatch(ScreenActions.ClearTextField) },
                    leadingIcon = {
                        IconButton(onBackPress) {
                            Icon(KesiIcons.ArrowBack, "")
                        }
                    },
                    placeHolder = "Search",
                    modifier = Modifier
                        .weight(1f)

                )

                AnimatedVisibility(
                    uiState.selectedItems.isNotEmpty(),
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    IconButton(
                        {
                            onEmotionsSelected(
                                emotionPicker.entryDraftId,
                                uiState.selectedItems.toList(),
                                emotionPicker.emotionType
                            )
                        }, colors = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(KesiIcons.Check, "")
                    }
                }
            }
        }
    }
    scaffoldDefinitionState.defineFabComposable {
        FloatingActionButton(
            onClick = {
                onCreateEmotionClick(emotionPicker.entryDraftId, emotionPicker.emotionType)
            }, Modifier
                .imePadding()
        ) {
            Icon(KesiIcons.Add, contentDescription = "Add")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dispatch(viewModel.dispatch(ScreenActions.SelectItems(emotionPicker.selectedEmotions)))
    }

    EmotionPickerScreen(
        uiState,
        { e -> viewModel.dispatch(ScreenActions.SelectionChange(e)) },
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmotionPickerScreen(
    uiState: EmotionPickerUiState,
    onEmotionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
    ) {
        uiState.emotions.map { item ->
            val isSelected = uiState.selectedItems.contains(item.id)
            EmotionItem(item, onEmotionClick, isSelected)
        }
    }
}

@Composable
fun EmotionItem(
    emotion: Emotion,
    onEmotionClick: (String) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedColor = when (emotion.valence) {
        Valence.NEGATIVE -> FilterChipDefaults.filterChipColors().copy(
            selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
        )

        Valence.NEUTRAL -> FilterChipDefaults.filterChipColors().copy(
            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        )

        Valence.POSITIVE -> FilterChipDefaults.filterChipColors().copy(
            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
        )
    }

    FilterChip(isSelected, {
        onEmotionClick(emotion.id)
    }, {
        Text(emotion.name, style = MaterialTheme.typography.bodyLarge)
    }, modifier = modifier,
        colors = selectedColor,
        shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart)
    )
}