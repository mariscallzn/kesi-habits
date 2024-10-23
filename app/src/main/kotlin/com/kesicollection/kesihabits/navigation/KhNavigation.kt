package com.kesicollection.kesihabits.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.addentry.navigation.addEntryScreen
import com.kesicollection.feature.addentry.navigation.navigateToAddEntry
import com.kesicollection.feature.habitpicker.navigation.habitPickerScreen
import com.kesicollection.feature.habitpicker.navigation.navigateToHabitPicker
import com.kesicollection.feature.weeklyhabits.navigation.WeeklyHabits
import com.kesicollection.feature.weeklyhabits.navigation.weeklyHabitsScreen

@Composable
fun KhNavHost(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    modifier: Modifier = Modifier
) {
    val startDestination = WeeklyHabits
    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {
        weeklyHabitsScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            addEntryClick = navController::navigateToAddEntry,
            modifier = modifier
        )
        addEntryScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onAddHabitClick = navController::navigateToHabitPicker,
            modifier = modifier
        )
        habitPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            modifier = modifier,
        )
    }
}