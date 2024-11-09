package com.kesicollection.influencerpicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.component.SearchTopBar
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.designsystem.utils.statusBarPaddingValues
import com.kesicollection.core.model.Influencer
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
        scaffoldDefinitionState.defineAppBarComposable {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = statusBarPaddingValues().calculateTopPadding(),
                        bottom = 0.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                SearchTopBar(
                    value = viewModel.searchTerm,
                    onValueChange = { v -> viewModel.updateSearchTerm(v) },
                    onCancel = { viewModel.dispatch(ScreenActions.ClearTextField) },
                    leadingIcon = {
                        IconButton(onBackPressed) {
                            Icon(KesiIcons.ArrowBack, "")
                        }
                    },
                    placeHolder = "Search",
                    modifier = Modifier
                        .weight(1f)

                )

                AnimatedVisibility(
                    uiState.selectedItems.isNotEmpty() || uiState.isEditing,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    IconButton(
                        {
                            onInfluencersSelected(
                                influencerPicker.entryDraftId,
                                uiState.selectedItems.toList(),
                            )
                        }, colors = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(KesiIcons.Check, "")
                    }
                }
            }
        }
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

    LaunchedEffect(Unit) {
        if (uiState.selectedItems.isEmpty() && influencerPicker.selectedInfluencerIds.isNotEmpty()) {
            viewModel.dispatch(viewModel.dispatch(ScreenActions.SelectItems(influencerPicker.selectedInfluencerIds)))
        }
    }

    InfluencerPickerScreen(
        uiState = uiState,
        onInfluencerClick = { e -> viewModel.dispatch(ScreenActions.SelectionChange(e)) },
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InfluencerPickerScreen(
    uiState: InfluencerPickerUiState,
    onInfluencerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
    ) {
        uiState.displayedInfluencers.map { item ->
            val isSelected = uiState.selectedItems.contains(item.id)
            InfluencerItem(item, onInfluencerClick, isSelected)
        }
    }
}

@Composable
fun InfluencerItem(
    influencer: Influencer,
    onInfluencerClick: (String) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    FilterChip(isSelected, {
        onInfluencerClick(influencer.id)
    }, {
        Text(influencer.name, style = MaterialTheme.typography.bodyLarge)
    }, modifier = modifier,
        colors = FilterChipDefaults.filterChipColors().copy(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart)
    )
}