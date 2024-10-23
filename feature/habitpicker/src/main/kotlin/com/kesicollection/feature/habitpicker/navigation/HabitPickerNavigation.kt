package com.kesicollection.feature.habitpicker.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.habitpicker.HabitPickerScreen
import kotlinx.serialization.Serializable

@Serializable
data object HabitPicker

fun NavController.navigateToHabitPicker(navOptions: NavOptions? = null) =
    navigate(route = HabitPicker, navOptions)

fun NavGraphBuilder.habitPickerScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<HabitPicker> {
        HabitPickerScreen(scaffoldDefinitionState, onBackPressed, modifier)
    }
}