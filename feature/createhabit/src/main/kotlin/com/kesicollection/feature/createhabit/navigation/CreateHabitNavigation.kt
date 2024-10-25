package com.kesicollection.feature.createhabit.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.createhabit.CreateHabitScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateHabit

fun NavController.navigateToCreateHabit(navOptions: NavOptions? = null) =
    navigate(route = CreateHabit, navOptions)

fun NavGraphBuilder.createHabitScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<CreateHabit> {
        CreateHabitScreen(
            scaffoldDefinitionState,
            onBackPressed,
            modifier
        )
    }
}