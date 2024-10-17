package com.kesicollection.feature.addentry.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.addentry.AddEntryScreen
import kotlinx.serialization.Serializable

@Serializable
data object AddEntry

fun NavController.navigateToAddEntry(navOptions: NavOptions? = null) =
    navigate(route = AddEntry, navOptions)

fun NavGraphBuilder.addEntryScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable<AddEntry> {
        AddEntryScreen(scaffoldDefinitionState, onBackPressed, modifier)
    }
}