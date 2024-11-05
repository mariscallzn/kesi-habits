package com.kesicollection.feature.createinfluencer.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.createinfluencer.CreateInfluencerScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias InfluencerId = String

@Serializable
data class CreateInfluencer(val entryDraftId: EntryDraftId)

fun NavController.navigateToCreateInfluencer(
    createInfluencer: CreateInfluencer,
    navOptions: NavOptions? = null
) =
    navigate(route = createInfluencer, navOptions)

fun NavGraphBuilder.createInfluencerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onCreateInfluencerClick: (EntryDraftId, InfluencerId) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<CreateInfluencer> { backStackEntry ->
        val createInfluencer = backStackEntry.toRoute<CreateInfluencer>()
        CreateInfluencerScreen(
            scaffoldDefinitionState,
            onBackPressed,
            createInfluencer,
            onCreateInfluencerClick,
            modifier
        )
    }
}