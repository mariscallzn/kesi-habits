package com.kesicollection.feature.addentry.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.addentry.AddEntryScreen
import kotlinx.serialization.Serializable

@Serializable
data class AddEntry(
    val draftId: String? = null,
    val habitId: String? = null,
    val habitType: HabitType? = null,
    val emotionIds: List<String> = emptyList(),
    val emotionType: EmotionType? = null,
    val influencerIds: List<String>? = null
)

internal typealias EntryDraftId = String
internal typealias HabitId = String
internal typealias EmotionIds = String
internal typealias InfluencersIds = String

fun NavController.navigateToAddEntry(addEntry: AddEntry, navOptions: NavOptions? = null) =
    navigate(route = addEntry, navOptions)

fun NavGraphBuilder.addEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onAddHabitClick: (EntryDraftId, HabitId? ,HabitType) -> Unit,
    onAddEmotionClick: (EntryDraftId, List<EmotionIds>, EmotionType) -> Unit,
    onAddInfluencerClick: (EntryDraftId, List<InfluencersIds>) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<AddEntry> { backStackEntry ->
        val addEntry = backStackEntry.toRoute<AddEntry>()
        AddEntryScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPress = onBackPressed,
            addEntry = addEntry,
            onAddHabitClick = onAddHabitClick,
            onAddEmotionClick = onAddEmotionClick,
            onAddInfluencerClick = onAddInfluencerClick,
            modifier = modifier
        )
    }
}