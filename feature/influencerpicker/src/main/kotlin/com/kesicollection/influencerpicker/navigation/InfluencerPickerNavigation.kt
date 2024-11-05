package com.kesicollection.influencerpicker.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.influencerpicker.InfluencerPickerScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias InfluencerId = String

@Serializable
data class InfluencerPicker(
    val entryDraftId: EntryDraftId,
    val selectedInfluencerIds: List<String>
)

fun NavController.navigateToInfluencerPicker(
    influencePicker: InfluencerPicker,
    navOptions: NavOptions? = null
) =
    navigate(route = influencePicker, navOptions)

fun NavGraphBuilder.influencerPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onCreateInfluencerClick: (EntryDraftId) -> Unit,
    onInfluencersSelected: (EntryDraftId, List<InfluencerId>) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<InfluencerPicker> { backStackEntry ->
        val influencerPicker = backStackEntry.toRoute<InfluencerPicker>()
        InfluencerPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = onBackPressed,
            influencerPicker = influencerPicker,
            onCreateInfluencerClick = onCreateInfluencerClick,
            onInfluencersSelected = onInfluencersSelected,
            modifier = modifier
        )

    }
}