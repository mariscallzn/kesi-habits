package com.kesicollection.feature.habitpicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.SearchTopBar
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.utils.statusBarPaddingValues
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.habitpicker.navigation.EntryDraftId
import com.kesicollection.feature.habitpicker.navigation.HabitId
import com.kesicollection.feature.habitpicker.navigation.HabitPicker

@Composable
fun HabitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    habitPicker: HabitPicker,
    onCreateHabitClick: (EntryDraftId, HabitType) -> Unit,
    onHabitSelected: (EntryDraftId, HabitId, HabitType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitPickerViewModel = hiltViewModel()
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
            }
        }
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(onClick = {
                onCreateHabitClick(habitPicker.entryDraftId, habitPicker.habitType)
            }) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }

    LaunchedEffect(habitPicker.selectedId) {
        if (habitPicker.selectedId?.isNotBlank() == true) {
            viewModel.dispatch(ScreenActions.SelectHabit(habitPicker.selectedId))
        }
    }

    HabitPickerScreen(
        habitPicker = habitPicker,
        onHabitSelected = onHabitSelected,
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun HabitPickerScreen(
    habitPicker: HabitPicker,
    onHabitSelected: (EntryDraftId, HabitId, HabitType) -> Unit,
    uiState: HabitPickerUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(uiState.displayedHabits) {
            HabitItem(
                habit = it,
                onClick = {
                    onHabitSelected(
                        habitPicker.entryDraftId,
                        it.id,
                        habitPicker.habitType
                    )
                },
                isSelected = uiState.selectedHabitId == it.id
            )

            Spacer(Modifier.padding(8.dp))
        }
    }
}

@Composable
fun HabitItem(
    habit: Habit,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor: Pair<Color, Color> = when (habit.classification) {
        Classification.POSITIVE -> Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer
        )

        Classification.NEGATIVE -> Pair(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer
        )

        Classification.NEUTRAL -> Pair(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    }

    Surface(
        border = BorderStroke(
            2.dp,
            color = if (isSelected) containerColor.first else MaterialTheme.colorScheme.primaryContainer
        ),
        color = if (isSelected) containerColor.first else MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = if (isSelected) containerColor.second else MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart),
        modifier = modifier
            .clickable { onClick() }
    ) {
        val classificationColor: Pair<Color, Color> = when (habit.classification) {
            Classification.POSITIVE -> Pair(
                MaterialTheme.colorScheme.onTertiary,
                MaterialTheme.colorScheme.tertiary
            )

            Classification.NEGATIVE -> Pair(
                MaterialTheme.colorScheme.onError,
                MaterialTheme.colorScheme.error
            )

            Classification.NEUTRAL -> Pair(
                MaterialTheme.colorScheme.onSecondary,
                MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Text(habit.name, modifier.weight(1f))
            Surface(
                shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart),
                contentColor = classificationColor.first,
                color = classificationColor.second
            ) {
                Text(
                    habit.classification.name,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                ) //TODO: i18
            }
        }
    }
}