package com.kesicollection.kesihabits.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.feature.weeklyhabits.navigation.WeeklyHabits
import com.kesicollection.feature.weeklyhabits.navigation.weeklyHabitsScreen

@Composable
fun KhNavHost(
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    setFabVisible: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val startDestination = WeeklyHabits
    val navController = rememberNavController()

    navController.addOnDestinationChangedListener { _, destination, _ ->
        //We only want to show the FAB button for WeeklyHabits screen
        setFabVisible(destination.route?.equals(WeeklyHabits::class.qualifiedName) == true)
    }

    NavHost(navController, startDestination = startDestination) {
        weeklyHabitsScreen(setAppBarTitle, setFabOnClick, modifier)
    }
}