package com.kesicollection.feature.addentry

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
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

@Composable
fun AddEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    onAddHabitClick: () -> Unit,
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

    AddEntryScreen(
        onAddHabitClick,
        uiState,
        modifier
    )
}

@Composable
fun AddEntryScreen(
    onAddHabitClick: () -> Unit,
    uiState: AddEntryUiState,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        HabitSection(onAddHabitClick)
        TriggeredSection(onAddHabitClick)
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth(),

            ) {
            CurrentEmotionsSection(modifier = Modifier.weight(1f))
            DesireEmotionsSection(modifier = Modifier.weight(1f))
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreationButton({}, Modifier.fillMaxWidth(), "add")
        }
    }
}

@Composable
fun HabitSection(onAddHabitClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.select_habit),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DashedButton("", modifier = Modifier.fillMaxWidth(), onAddHabitClick)
    }
}

@Composable
fun TriggeredSection(onAddHabitClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "What habit triggered you?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DashedButton("", modifier = Modifier.fillMaxWidth(), onAddHabitClick)
    }
}

@Composable
fun CurrentEmotionsSection(modifier: Modifier = Modifier) {
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
        ) { }
    }
}

@Composable
fun DesireEmotionsSection(modifier: Modifier = Modifier) {
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
        ) { }
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
private fun HabitSectionPreview() {
    KesiTheme {
        AddEntryScreen(
            {}, AddEntryUiState(true)
        )
    }
}