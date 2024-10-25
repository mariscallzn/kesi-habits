package com.kesicollection.feature.habitpicker

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
import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.habitpicker.navigation.HabitPicker

//TODO: Review
//https://www.lordcodes.com/articles/compose-embed-searchbar-topappbar/

@Composable
fun HabitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    habitPicker: HabitPicker,
    onCreateHabitClick: (HabitType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HabitPickerScreen(
        scaffoldDefinitionState,
        onBackPress,
        habitPicker,
        onCreateHabitClick,
        uiState,
        modifier
    )
}

@Composable
fun HabitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    habitPicker: HabitPicker,
    onCreateHabitClick: (HabitType) -> Unit,
    uiState: HabitPickerUiState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineAppBarComposable {
          CommonTopAppBar(onBackPress, "Habit picker")
        }
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(onClick = {
                onCreateHabitClick(habitPicker.habitType)
            }) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }
}