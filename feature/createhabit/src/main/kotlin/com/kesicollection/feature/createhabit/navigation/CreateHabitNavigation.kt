package com.kesicollection.feature.createhabit.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.createhabit.CreateHabitScreen
import kotlinx.serialization.Serializable

internal typealias EntryDraftId = String
internal typealias HabitId = String

@Serializable
data class CreateHabit(val type: HabitType, val entryDraftId: EntryDraftId)

fun NavController.navigateToCreateHabit(createHabit: CreateHabit, navOptions: NavOptions? = null) =
    navigate(route = createHabit, navOptions)

fun NavGraphBuilder.createHabitScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    onHabitCreated: (EntryDraftId, HabitId, HabitType) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<CreateHabit> { backStackEntry ->
        val createHabit = backStackEntry.toRoute<CreateHabit>()
        CreateHabitScreen(
            scaffoldDefinitionState,
            onBackPressed,
            createHabit,
            onHabitCreated,
            modifier
        )
    }
}