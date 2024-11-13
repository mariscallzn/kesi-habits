package com.kesicollection.feature.weeklyhabits.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.weeklyhabits.WeeklyHabitsScreen
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyHabits(val selectedDate: String? = null)

fun NavController.navigateToWeeklyHabits(
    weeklyHabits: WeeklyHabits,
    navOptions: NavOptions? = null
) =
    navigate(route = weeklyHabits, navOptions)

fun NavGraphBuilder.weeklyHabitsScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    addEntryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<WeeklyHabits> { backStackEntry ->
        val weeklyHabits = backStackEntry.toRoute<WeeklyHabits>()
        WeeklyHabitsScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            weeklyHabits = weeklyHabits,
            addEntryClick = addEntryClick,
            modifier = modifier
        )
    }
}