package com.kesicollection.feature.habitpicker.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.habitpicker.HabitPickerScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias HabitId = String

@Serializable
data class HabitPicker(
    val habitType: HabitType,
    val entryDraftId: EntryDraftId,
    val selectedId: String? = null
)

fun NavController.navigateToHabitPicker(habitPicker: HabitPicker, navOptions: NavOptions? = null) =
    navigate(route = habitPicker, navOptions)

fun NavGraphBuilder.habitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onCreateHabitClick: (EntryDraftId, HabitType) -> Unit,
    onHabitSelected: (EntryDraftId, HabitId, HabitType) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<HabitPicker> { backStackEntry ->
        val habitPicker = backStackEntry.toRoute<HabitPicker>()
        HabitPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPress = onBackPressed,
            habitPicker = habitPicker,
            onCreateHabitClick = onCreateHabitClick,
            onHabitSelected = onHabitSelected,
            modifier = modifier
        )
    }
}