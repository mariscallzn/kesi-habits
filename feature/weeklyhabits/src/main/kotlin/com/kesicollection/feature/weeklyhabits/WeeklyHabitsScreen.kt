package com.kesicollection.feature.weeklyhabits

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Stateless with no ViewModel
 */
@Composable
internal fun WeeklyHabitsScreen(
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("Hello, world")
}

// State-full with viewmodel
//@Composable
//internal fun WeeklyHabitsScreen () {
//
//}