package com.kesicollection.feature.addentry

import android.util.Log
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            TopAppBar(title = {
                Text(text = "Use string resources")
            }, navigationIcon = {
                IconButton(onBackPress) {
                    Icon(KesiIcons.ArrowBack, "use string resources")
                }
            })
        }
    }

    AddEntryScreen(uiState, modifier)

}

@Composable
fun AddEntryScreen(
    uiState: AddEntryUiState,
    modifier: Modifier = Modifier
) {

    Text("UiState: $uiState", modifier = modifier)
}