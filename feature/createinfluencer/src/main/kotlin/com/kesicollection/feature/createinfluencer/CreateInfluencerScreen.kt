package com.kesicollection.feature.createinfluencer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.kesicollection.feature.createinfluencer.navigation.CreateInfluencer
import com.kesicollection.feature.createinfluencer.navigation.EntryDraftId
import com.kesicollection.feature.createinfluencer.navigation.InfluencerId

@Composable
fun CreateInfluencerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    createInfluencer: CreateInfluencer,
    onCreateInfluencerClick: (EntryDraftId, InfluencerId) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateInfluencerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scaffoldDefinitionState.defineFabComposable(null)
        scaffoldDefinitionState.defineAppBarComposable {
            CommonTopAppBar(onBackPressed)
        }
    }

    LaunchedEffect(uiState.createdInfluencerId) {
        if (uiState.createdInfluencerId.isNotBlank()) {
            onCreateInfluencerClick(createInfluencer.entryDraftId, uiState.createdInfluencerId)
        }
    }

    CreateInfluencerScreen(
        uiState = uiState,
        influencerName = viewModel.influencerName,
        onInfluencerNameChangeValue = { v -> viewModel.updateInfluencerName(v) },
        onCancel = { viewModel.dispatch(ScreenActions.ClearText) },
        onCreate = { viewModel.dispatch(viewModel.createInfluencer(Unit)) },
        modifier = modifier
    )
}

@Composable
fun CreateInfluencerScreen(
    uiState: CreateInfluencerUiState,
    influencerName: String,
    onInfluencerNameChangeValue: (String) -> Unit,
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
            influencerName,
            onInfluencerNameChangeValue,
            placeHolder = "Influencer name",
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 120.dp, bottom = 16.dp),
            onCancel = onCancel
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreationButton(
                onClick = onCreate,
                enabled = uiState.isCreateEnable,
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(com.kesicollection.core.designsystem.R.string.create)
            )
        }
    }
}