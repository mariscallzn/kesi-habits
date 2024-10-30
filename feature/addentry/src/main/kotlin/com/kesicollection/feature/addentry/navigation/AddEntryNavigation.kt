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
    val emotionId: String? = null,//TODO: make it a list so you can receive more from the picker screen
    val emotionType: EmotionType? = null,
)

internal typealias EntryDraftId = String

fun NavController.navigateToAddEntry(addEntry: AddEntry, navOptions: NavOptions? = null) =
    navigate(route = addEntry, navOptions)

fun NavGraphBuilder.addEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onAddHabitClick: (EntryDraftId, HabitType) -> Unit,
    onAddEmotionClick: (EntryDraftId, EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<AddEntry> { backStackEntry ->
        val addEntry = backStackEntry.toRoute<AddEntry>()
        AddEntryScreen(
            scaffoldDefinitionState,
            onBackPressed,
            addEntry,
            onAddHabitClick,
            onAddEmotionClick,
            modifier
        )
    }
}