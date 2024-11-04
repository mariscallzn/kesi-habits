package com.kesicollection.feature.addentry

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.CommonTopAppBar
import com.kesicollection.core.designsystem.component.CreationButton
import com.kesicollection.core.designsystem.component.DashedBox
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.core.model.Arousal
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Emotion
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.HabitType
import com.kesicollection.core.model.Status
import com.kesicollection.core.model.Valence
import com.kesicollection.feature.addentry.navigation.AddEntry
import com.kesicollection.feature.addentry.navigation.EmotionIds
import com.kesicollection.feature.addentry.navigation.EntryDraftId
import com.kesicollection.feature.addentry.utils.ClassificationBoxColor

@Composable
fun AddEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    addEntry: AddEntry,
    onAddHabitClick: (EntryDraftId, HabitType) -> Unit,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPress, stringResource(R.string.add_entry_screen))
        }
    }

    LaunchedEffect(addEntry) {
        when {
            addEntry.draftId.isNullOrBlank() &&
                    addEntry.habitId.isNullOrBlank() &&
                    addEntry.emotionIds.isEmpty() -> viewModel.dispatch(
                viewModel.createDraft(Unit)
            )

            addEntry.draftId?.isNotBlank() == true -> viewModel.dispatch(
                viewModel.dispatch(
                    viewModel.updateHabit(addEntry)
                )
            )
        }
    }

    AddEntryScreen(
        onAddHabitClick,
        onAddEmotionClick,
        onClearClick = { draftId, habitType ->
            viewModel.dispatch(
                viewModel.updateHabit(AddEntry(draftId = draftId, habitType = habitType))
            )
        },
        uiState,
        modifier
    )
}

@Composable
fun AddEntryScreen(
    onAddHabitClick: (EntryDraftId, HabitType) -> Unit,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    onClearClick: (EntryDraftId, HabitType) -> Unit,
    uiState: AddEntryUiState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_habit),
            style = MaterialTheme.typography.titleMedium,
        )
        if (uiState.coreHabit?.name?.isNotBlank() == true) HabitCard(
            uiState.draftId,
            onAddHabitClick,
            onClearClick,
            uiState.coreHabit,
            HabitType.CORE
        ) else DashedButton(
            "",
            modifier = Modifier.fillMaxWidth()
        ) {
            onAddHabitClick(
                uiState.draftId,
                HabitType.CORE
            )
        }

        Text(
            text = "What habit triggered you?",
            style = MaterialTheme.typography.titleMedium,
        )
        if (uiState.triggerHabit?.name?.isNotBlank() == true) HabitCard(
            uiState.draftId,
            onAddHabitClick,
            onClearClick,
            uiState.triggerHabit,
            HabitType.TRIGGER
        ) else DashedButton(
            "",
            modifier = Modifier.fillMaxWidth()
        ) {
            onAddHabitClick(
                uiState.draftId,
                HabitType.TRIGGER
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            CurrentEmotionsSection(
                uiState.draftId,
                onAddEmotionClick,
                uiState.currentEmotions,
                modifier = Modifier.weight(1f)
            )
            DesireEmotionsSection(
                uiState.draftId,
                onAddEmotionClick,
                uiState.desireEmotions,
                modifier = Modifier.weight(1f)
            )
        }
        CreationButton(
            enabled = uiState.isSaveEnabled,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            text = "add"
        )
    }
}

@Composable
fun HabitCard(
    draftEntryId: EntryDraftId,
    onAddHabitClick: (EntryDraftId, HabitType) -> Unit,
    onClearClick: (EntryDraftId, HabitType) -> Unit,
    habit: Habit,
    habitType: HabitType,
    modifier: Modifier = Modifier
) {
    val color: ClassificationBoxColor =
        when (habit.classification) {
            Classification.POSITIVE -> ClassificationBoxColor(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.tertiaryContainer,
                MaterialTheme.colorScheme.onTertiaryContainer,
                MaterialTheme.colorScheme.onTertiary,
            )

            Classification.NEUTRAL -> ClassificationBoxColor(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.onSecondaryContainer,
                MaterialTheme.colorScheme.onSecondary,
            )

            else -> ClassificationBoxColor(
                MaterialTheme.colorScheme.error,
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer,
                MaterialTheme.colorScheme.onError,
            )
        }
    Row(modifier = Modifier.fillMaxWidth()) {
        Surface(
            border = BorderStroke(2.dp, color = color.border),
            color = color.background,
            contentColor = color.container,
            shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart),
            modifier = modifier
                .clickable {
                    Log.d(TAG, "HabitSection: draftId $draftEntryId")
                    onAddHabitClick(draftEntryId, habitType)
                }
                .weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = habit.name, modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .background(
                            color.border,
                            shape = RoundedCornerShape(MaterialTheme.shapes.medium.topStart)
                        )
                ) {
                    Text(
                        text = habit.classification.name,
                        modifier = Modifier.padding(8.dp),
                        color = color.classification,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        IconButton(
            { onClearClick(draftEntryId, habitType) },
            colors = IconButtonDefaults.iconButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.outline
            )
        ) {
            Icon(KesiIcons.Cancel, "")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmotionCard(
    draftEntryId: EntryDraftId,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    emotionType: EmotionType,
    emotions: List<Emotion>,
    modifier: Modifier = Modifier
) {
    Surface(
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primaryContainer),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart),
        modifier = modifier
            .clickable { onAddEmotionClick(draftEntryId, emotions.map { it.id }, emotionType) }
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            emotions.map {
                EmotionItem(
                    it,
                    { onAddEmotionClick(draftEntryId, emotions.map { e -> e.id }, emotionType) })
            }
        }
    }
}

@Composable
fun EmotionItem(
    emotion: Emotion,
    onClick: () -> Unit,
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
    FilterChip(
        selected = true, onClick = onClick, label = {
            Text(emotion.name, style = MaterialTheme.typography.bodyLarge)
        },
        modifier = modifier,
        colors = selectedColor,
        shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart)
    )
}

@Composable
fun CurrentEmotionsSection(
    draftEntryId: EntryDraftId,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    emotions: List<Emotion>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Current feelings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (emotions.isEmpty())
            DashedButton(
                "",
                Modifier
                    .fillMaxWidth()
                    .heightIn(250.dp, Dp.Infinity)
            ) {
                onAddEmotionClick(
                    draftEntryId,
                    emptyList(),
                    EmotionType.CURRENT
                )
            } else EmotionCard(
            draftEntryId,
            onAddEmotionClick,
            EmotionType.CURRENT,
            emotions,
            Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DesireEmotionsSection(
    draftEntryId: EntryDraftId,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    emotions: List<Emotion>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Desire feelings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (emotions.isEmpty())
            DashedButton(
                "",
                Modifier
                    .fillMaxWidth()
                    .heightIn(250.dp, Dp.Infinity)
            ) {
                onAddEmotionClick(
                    draftEntryId,
                    emptyList(),
                    EmotionType.DESIRE
                )
            } else EmotionCard(
            draftEntryId, onAddEmotionClick, EmotionType.DESIRE, emotions, Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DashedButton(
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    DashedBox(
        MaterialTheme.colorScheme.primaryContainer,
        2.dp, 12.dp, 4.dp,
        innerPadding = 16.dp,
        cornerRadius = MaterialTheme.shapes.large,
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            KesiIcons.Add,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(MaterialTheme.shapes.medium.topStart)
                )
                .padding(8.dp)
        )
    }
}

@DarkLightPreviews
@Composable
private fun AddEntryScreenPreview() {
    KesiTheme {
        AddEntryScreen(
            onAddEmotionClick = { _, _, _ -> },
            onAddHabitClick = { _, _ -> },
            onClearClick = { _, _ -> },
            uiState = initialState.copy(
                currentEmotions = listOf(
                    Emotion("", "Happy", Valence.POSITIVE, Arousal.MODERATE, Status.ACTIVE),
                    Emotion("", "Serene", Valence.NEUTRAL, Arousal.MODERATE, Status.ACTIVE),
                    Emotion("", "Sad", Valence.NEGATIVE, Arousal.MODERATE, Status.ACTIVE),
                )
            )
        )
    }
}
