package com.kesicollection.feature.addentry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.CommonTopAppBar
import com.kesicollection.core.designsystem.component.CreationButton
import com.kesicollection.core.designsystem.component.DashedBox
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.HabitType
import com.kesicollection.core.model.HumanNeed
import com.kesicollection.core.model.Influencer
import com.kesicollection.feature.addentry.model.CreateDraftThunk
import com.kesicollection.feature.addentry.model.UpdateTimeThunk
import com.kesicollection.feature.addentry.navigation.AddEntry
import com.kesicollection.feature.addentry.navigation.EntryDraftId
import com.kesicollection.feature.addentry.navigation.HabitId
import com.kesicollection.feature.addentry.navigation.InfluencersIds
import com.kesicollection.feature.addentry.navigation.RecordedOn
import com.kesicollection.feature.addentry.utils.ClassificationBoxColor
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun AddEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    addEntry: AddEntry,
    onEntryCreated: (RecordedOn) -> Unit,
    onAddHabitClick: (EntryDraftId, HabitId?, HabitType) -> Unit,
    onAddInfluencerClick: (EntryDraftId, List<InfluencersIds>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val locales = LocalConfiguration.current.locales
    LaunchedEffect(locales[0]) {
        viewModel.dispatch(ScreenActions.DefineLocale(locales[0]))
    }

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPress, "${uiState.formattedTime} • ${uiState.formattedDate}") {
                IconButton({ viewModel.dispatch(ScreenActions.ShowTimeDialog(true)) }) {
                    Icon(KesiIcons.Time, "")
                }
                IconButton({ viewModel.dispatch(ScreenActions.ShowDateDialog(true)) }) {
                    Icon(KesiIcons.Date, "")
                }
            }
        }
    }

    LaunchedEffect(addEntry) {
        when {
            addEntry.draftId.isNullOrBlank() &&
                    addEntry.habitId.isNullOrBlank() -> viewModel.dispatch(
                viewModel.createDraft(
                    CreateDraftThunk(
                        OffsetDateTime.now(ZoneOffset.UTC),
                        locales[0]
                    )
                )
            )

            addEntry.draftId?.isNotBlank() == true -> viewModel.dispatch(
                viewModel.dispatch(
                    viewModel.updateHabit(addEntry)
                )
            )
        }
    }

    LaunchedEffect(uiState.recordedOn) {
        uiState.recordedOn?.let { onEntryCreated(it) }
    }

    AddEntryScreen(
        onAddEntry = { viewModel.dispatch(viewModel.draftFinished(Unit)) },
        onAddHabitClick = onAddHabitClick,
        onAddInfluencerClick = onAddInfluencerClick,
        onHumanNeedClick = { hn -> viewModel.dispatch(viewModel.updateHumanNeed(hn)) },
        onClearClick = { draftId, habitType ->
            viewModel.dispatch(
                viewModel.updateHabit(
                    AddEntry(
                        draftId = draftId,
                        habitType = habitType
                    )
                )
            )
        },
        onDismissDateDialog = { viewModel.dispatch(ScreenActions.ShowDateDialog(false)) },
        onDismissTimeDialog = { viewModel.dispatch(ScreenActions.ShowTimeDialog(false)) },
        onDateSelected = { millis ->
            viewModel.dispatch(
                ScreenActions.DateSelected(
                    millis,
                    locales[0]
                )
            )
            viewModel.dispatch(viewModel.updateDate(millis))
        },
        onTimeSelected = { hour, minute, isAfternoon ->
            viewModel.dispatch(
                ScreenActions.TimeSelected(
                    hour,
                    minute,
                    isAfternoon
                )
            )
            viewModel.dispatch(viewModel.updateTime(UpdateTimeThunk(hour, minute)))
        },
        uiState = uiState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onAddEntry: () -> Unit,
    onAddHabitClick: (EntryDraftId, HabitId?, HabitType) -> Unit,
    onAddInfluencerClick: (EntryDraftId, List<InfluencersIds>) -> Unit,
    onHumanNeedClick: (HumanNeed) -> Unit,
    onClearClick: (EntryDraftId, HabitType) -> Unit,
    onDismissDateDialog: () -> Unit,
    onDismissTimeDialog: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    onTimeSelected: (Int, Int, Boolean) -> Unit,
    uiState: AddEntryUiState,
    modifier: Modifier = Modifier
) {

    val dateDialogState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    LaunchedEffect(uiState.time) {
        timePickerState.hour = uiState.time.first
        timePickerState.minute = uiState.time.second
    }

    if (uiState.isTimeShowing) {
        TimePickerSwitchable(
            state = timePickerState,
            onCancel = onDismissTimeDialog,
            onConfirm = {
                onTimeSelected(
                    timePickerState.hour,
                    timePickerState.minute,
                    timePickerState.isAfternoon
                )
            }
        )
    }
    if (uiState.isDateShowing) {
        DatePickerDialog(
            onDismissRequest = onDismissDateDialog,
            confirmButton = {
                TextButton({ onDateSelected(dateDialogState.selectedDateMillis) }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onDismissDateDialog) {
                    Text("Dismiss")
                }
            }
        ) {
            DatePicker(dateDialogState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.select_habit),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (uiState.coreHabit?.name?.isNotBlank() == true) HabitCard(
                uiState.draftId,
                onAddHabitClick,
                onClearClick,
                uiState.coreHabit,
                HabitType.CORE
            ) else SelectButton(
                "Select habit",
                "Select habit",
                modifier = Modifier.fillMaxWidth()
            ) {
                onAddHabitClick(
                    uiState.draftId,
                    null,
                    HabitType.CORE,
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "What habit triggered you?",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (uiState.triggerHabit?.name?.isNotBlank() == true) HabitCard(
                uiState.draftId,
                onAddHabitClick,
                onClearClick,
                uiState.triggerHabit,
                HabitType.TRIGGER
            ) else SelectButton(
                "Optional",
                "Optional",
                modifier = Modifier.fillMaxWidth()
            ) {
                onAddHabitClick(
                    uiState.draftId,
                    null,
                    HabitType.TRIGGER
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "What influenced you to act?",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (uiState.influencers.isNotEmpty()) {
                InfluencerCard(uiState.influencers, {
                    onAddInfluencerClick(uiState.draftId, uiState.influencers.map { it.id })
                }, modifier = Modifier.fillMaxWidth())
            } else {
                SelectButton(
                    "Optional",
                    "Optional",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onAddInfluencerClick(uiState.draftId, emptyList())
                }
            }
        }

        Column {
            Text(
                text = "What are you seeking 1 to 6?",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            HumanNeeds(uiState.humanNeeds, onHumanNeedClick, modifier = Modifier.fillMaxWidth())
        }


        Box(modifier = Modifier.weight(1f)) {
            CreationButton(
                enabled = uiState.isSaveEnabled,
                onClick = onAddEntry,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "add"
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HumanNeeds(
    humanNeeds: List<HumanNeed>,
    onHumanNeedClick: (HumanNeed) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart)
            )
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            humanNeeds.map { hn ->
                FilterChip(selected = hn.ranked != -1, onClick = { onHumanNeedClick(hn) }, label = {
                    Row {
                        if (hn.ranked != -1) Text("#${hn.ranked}")
                        Text(hn.name)
                    }
                },
                    shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HabitCard(
    draftEntryId: EntryDraftId,
    onAddHabitClick: (EntryDraftId, HabitId, HabitType) -> Unit,
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
                    onAddHabitClick(draftEntryId, habit.id, habitType)
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
fun InfluencerCard(
    influencer: List<Influencer>,
    onContainerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfoContainer(onContainerClick, modifier = modifier) {
        influencer.map {
            FilterChip(
                selected = true, onClick = onContainerClick, label = {
                    Text(it.name, style = MaterialTheme.typography.bodyLarge)
                },
                colors = FilterChipDefaults.filterChipColors().copy(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InfoContainer(
    onContainerClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primaryContainer),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart),
        modifier = modifier
            .clickable { onContainerClick() }
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) { content() }
    }
}

@Composable
fun SelectButton(
    description: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    DashedBox(
        MaterialTheme.colorScheme.primaryContainer,
        2.dp, 12.dp, 0.dp,
        cornerRadius = MaterialTheme.shapes.small,
        onClick = onClick,
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text(description, modifier = Modifier.weight(1f))
            Icon(
                KesiIcons.ChevronRight,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSwitchable(
    state: TimePickerState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val showingPicker = remember { mutableStateOf(true) }
    TimePickerDialog(onCancel = onCancel, onConfirm = onConfirm, toggle = {
        if (configuration.screenHeightDp > 400) {
            IconButton(onClick = { showingPicker.value = !showingPicker.value }) {
                val icon = if (showingPicker.value) {
                    KesiIcons.Keyboard
                } else {
                    KesiIcons.Schedule
                }
                Icon(
                    icon,
                    contentDescription =
                    if (showingPicker.value) {
                        "Switch to Text Input"
                    } else {
                        "Switch to Touch Input"
                    }
                )
            }
        }
    }) {
        if (showingPicker.value && configuration.screenHeightDp > 400) {
            TimePicker(state = state)
        } else {
            TimeInput(state = state)
        }
    }

}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
            Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun AddEntryScreenPreview() {
    KesiTheme {
        AddEntryScreen(
            onAddEntry = {},
            onAddHabitClick = { _, _, _ -> },
            onAddInfluencerClick = { _, _ -> },
            onHumanNeedClick = { _ -> },
            onClearClick = { _, _ -> },
            onDismissDateDialog = {},
            onDateSelected = {},
            onDismissTimeDialog = {},
            onTimeSelected = { _, _, _ -> },
            uiState = initialState
        )
    }
}
