package com.kesicollection.feature.addentry

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.HabitType
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
            if (uiState.currentEmotions.isEmpty()) CurrentEmotionsSection(
                uiState.draftId,
                onAddEmotionClick,
                modifier = Modifier.weight(1f)
            ) else Text(text = uiState.currentEmotions.joinToString { emotion -> emotion.name + " " })
            if (uiState.desireEmotions.isEmpty()) DesireEmotionsSection(
                uiState.draftId,
                onAddEmotionClick, modifier = Modifier.weight(1f)
            ) else Text(text = uiState.desireEmotions.joinToString { emotion -> emotion.name + " " })
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreationButton(
                enabled = uiState.isSaveEnabled,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                text = "add"
            )
        }
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

@Composable
fun CurrentEmotionsSection(
    draftEntryId: EntryDraftId,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Current feelings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DashedButton(
            "",
            Modifier
                .fillMaxWidth()
                .heightIn(250.dp, Dp.Infinity)
        ) { onAddEmotionClick(draftEntryId, /*TODO*/ emptyList(), EmotionType.CURRENT) }
    }
}

@Composable
fun DesireEmotionsSection(
    draftEntryId: EntryDraftId,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Desire feelings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DashedButton(
            "",
            Modifier
                .fillMaxWidth()
                .heightIn(250.dp, Dp.Infinity)
        ) { onAddEmotionClick(draftEntryId, /*TODO*/ emptyList(), EmotionType.DESIRE) }
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
