package com.kesicollection.feature.createhabit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState

@Composable
fun CreateHabitScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CreateHabitScreen(scaffoldDefinitionState, onBackPressed, uiState, modifier)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    uiState: CreateHabitUiState,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            TopAppBar(title = {
                Text(
                    text = "Create habit",
                    style = MaterialTheme.typography.headlineMedium
                )
            }, navigationIcon = {
                IconButton(onBackPress) {
                    Icon(KesiIcons.ArrowBack, "")
                }
            })
        }
    }
    Text("Create Habit", modifier)
}