package com.kesicollection.feature.emotionpicker.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.EmotionType
import com.kesicollection.feature.emotionpicker.EmotionPickerScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias EmotionId = String

@Serializable
data class EmotionPicker(
    val entryDraftId: EntryDraftId,
    val emotionType: EmotionType,
    val selectedEmotions: List<String> = emptyList()
)

fun NavController.navigateToEmotionPicker(
    emotionPicker: EmotionPicker,
    navOptions: NavOptions? = null
) = navigate(route = emotionPicker, navOptions)

fun NavGraphBuilder.emotionPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onCreateEmotionClick: (EntryDraftId, EmotionType) -> Unit,
    onEmotionsSelected: (EntryDraftId, List<EmotionId>, EmotionType) -> Unit,
    modifier: Modifier,
) {
    composable<EmotionPicker> { backStackEntry ->
        val emotionPicker = backStackEntry.toRoute<EmotionPicker>()
        EmotionPickerScreen(
            scaffoldDefinitionState,
            onBackPressed,
            emotionPicker,
            onCreateEmotionClick,
            onEmotionsSelected,
            modifier
        )
    }
}