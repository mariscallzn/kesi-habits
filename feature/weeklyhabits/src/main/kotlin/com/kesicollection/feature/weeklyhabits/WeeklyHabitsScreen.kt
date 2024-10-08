package com.kesicollection.feature.weeklyhabits

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    WeeklyHabitsScreen(uiState, setAppBarTitle, setFabOnClick, modifier)
}
