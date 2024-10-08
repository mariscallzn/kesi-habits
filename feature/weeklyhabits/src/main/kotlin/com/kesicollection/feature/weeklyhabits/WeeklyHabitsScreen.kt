package com.kesicollection.feature.weeklyhabits

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * State full
 */
@Composable
internal fun WeeklyHabitsScreen(
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeeklyHabitsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        setFabOnClick {
            viewModel.addHabit()
        }
    }

    WeeklyHabitsScreen(uiState, setAppBarTitle, setFabOnClick, modifier)
}

/**
 * Stateless
 */
@Composable
internal fun WeeklyHabitsScreen(
    uiState: WeeklyHabitsUiState,
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("Hello, world $uiState")
}


