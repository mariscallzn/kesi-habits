package com.kesicollection.feature.createhabit

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
import com.kesicollection.core.model.Classification

@Composable
fun CreateHabitScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPressed)
        }
    }

    CreateHabitScreen(
        uiState = uiState,
        classification = viewModel.classification,
        onClassificationChange = { value -> viewModel.updateClassification(value) },
        habitName = viewModel.habitName,
        habitNameChangeValue = { value -> viewModel.updateHabitName(value) },
        onCancel = { viewModel.dispatch(ScreenActions.ClearText) },
        onCreate = {},
        modifier = modifier
    )
}

@Composable
fun CreateHabitScreen(
    uiState: CreateHabitUiState,
    classification: Classification?,
    onClassificationChange: (Classification) -> Unit,
    habitName: String,
    habitNameChangeValue: (String) -> Unit,
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
            habitName,
            habitNameChangeValue,
            placeHolder = stringResource(R.string.new_habit),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 120.dp, bottom = 16.dp),
            onCancel = onCancel
        )
        Text(
            text = stringResource(R.string.habit_classification_title),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterChip(
                shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
                selected = classification == Classification.POSITIVE,
                onClick = { onClassificationChange(Classification.POSITIVE) },
                label = { Text(stringResource(R.string.positive).uppercase()) },
                colors = FilterChipDefaults.filterChipColors()
                    .copy(
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
            )
            FilterChip(
                shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
                selected = classification == Classification.NEUTRAL,
                onClick = { onClassificationChange(Classification.NEUTRAL) },
                label = { Text(stringResource(R.string.neutral).uppercase()) }
            )
            FilterChip(
                shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
                selected = classification == Classification.NEGATIVE,
                onClick = { onClassificationChange(Classification.NEGATIVE) },
                label = { Text(stringResource(R.string.negative).uppercase()) },
                colors = FilterChipDefaults.filterChipColors()
                    .copy(
                        selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer
                    ),
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreationButton(
                onClick = onCreate,
                enabled = uiState.isCreateButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(com.kesicollection.core.designsystem.R.string.create)
            )
        }
    }
}