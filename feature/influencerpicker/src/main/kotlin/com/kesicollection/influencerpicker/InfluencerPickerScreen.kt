package com.kesicollection.influencerpicker

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.influencerpicker.navigation.EntryDraftId
import com.kesicollection.influencerpicker.navigation.InfluencerId
import com.kesicollection.influencerpicker.navigation.InfluencerPicker

@Composable
fun InfluencerPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    influencerPicker: InfluencerPicker,
    onCreateInfluencerClick: (EntryDraftId) -> Unit,
    onInfluencersSelected: (EntryDraftId, List<InfluencerId>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InfluencerPickerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(
                onClick = {
                    onCreateInfluencerClick(influencerPicker.entryDraftId)
                }, Modifier
                    .imePadding()
            ) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }

    InfluencerPickerScreen(modifier = modifier)
}

@Composable
fun InfluencerPickerScreen(modifier: Modifier = Modifier) {

}