package com.kesicollection.feature.habitpicker

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState

//TODO: Review
//https://www.lordcodes.com/articles/compose-embed-searchbar-topappbar/

@Composable
fun HabitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    onCreateHabitClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HabitPickerScreen(
        scaffoldDefinitionState,
        onBackPress,
        onCreateHabitClick,
        uiState,
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    onCreateHabitClick: () -> Unit,
    uiState: HabitPickerUiState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineAppBarComposable {
            Text(
                "TODO: Search bar TODO TODO TODO TODO Camera?",
                modifier = Modifier
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                    .padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(onClick = onCreateHabitClick) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }
}