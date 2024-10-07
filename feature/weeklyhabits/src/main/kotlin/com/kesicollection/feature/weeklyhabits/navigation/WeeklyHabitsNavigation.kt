package com.kesicollection.feature.weeklyhabits.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.feature.weeklyhabits.WeeklyHabitsScreen
import kotlinx.serialization.Serializable

@Serializable
data object WeeklyHabits

fun NavController.navigateToWeeklyHabits(navOptions: NavOptions) =
    navigate(route = WeeklyHabits, navOptions)

fun NavGraphBuilder.weeklyHabitsScreen(
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable<WeeklyHabits> {
        //TODO Viewmodel one
        WeeklyHabitsScreen(setAppBarTitle, setFabOnClick, modifier)
    }
}