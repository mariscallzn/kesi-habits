package com.kesicollection.feature.createemotion.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.EmotionType
import com.kesicollection.feature.createemotion.CreateEmotionScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias EmotionId = String

@Serializable
data class CreateEmotion(val draftId: EntryDraftId, val emotionType: EmotionType)

fun NavController.navigateToCreateEmotion(
    createEmotion: CreateEmotion,
    navOptions: NavOptions? = null
) = navigate(route = createEmotion, navOptions)

fun NavGraphBuilder.createEmotionScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onCreateEmotionClick: (EntryDraftId, EmotionId, EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<CreateEmotion> { backStackEntry ->
        val createEmotion = backStackEntry.toRoute<CreateEmotion>()
        CreateEmotionScreen(
            scaffoldDefinitionState, onBackPressed, createEmotion, onCreateEmotionClick, modifier
        )
    }
}