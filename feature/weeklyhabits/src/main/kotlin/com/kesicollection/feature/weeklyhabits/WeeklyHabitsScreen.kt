package com.kesicollection.feature.weeklyhabits

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.weeklyhabits.navigation.WeeklyHabits

/**
 * State full
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeeklyHabitsScreen(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    weeklyHabits: WeeklyHabits,
    addEntryClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeeklyHabitsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("Andres", "WeeklyHabitsScreen: $weeklyHabits")
        viewModel.dispatch(ScreenAction.LoadCalendar())
        scaffoldDefinitionState.defineAppBarComposable {
            TopAppBar(title = {
                Text(text = "Use string resources")
            })
        }
        scaffoldDefinitionState.defineFabComposable {
            FloatingActionButton(onClick = addEntryClick) {
                Icon(KesiIcons.Add, contentDescription = "Add")
            }
        }
    }

    WeeklyHabitsScreen(uiState, scaffoldDefinitionState, { i ->
        viewModel.dispatch(viewModel.watchPagingIndex(i))
    }, modifier)
}

/**
 * Stateless
 */
@Composable
internal fun WeeklyHabitsScreen(
    uiState: WeeklyHabitsUiState,
    scaffoldDefinitionState: ScaffoldDefinitionState,
    watchRealIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val pagerState =
        rememberPagerState(
            pageCount = { Int.MAX_VALUE },
            initialPage = (Int.MAX_VALUE / 2)
        )

    HorizontalPager(state = pagerState) { page ->
        val computeIndex = uiState.offsetIndex + (page - (Int.MAX_VALUE / 2))
        if (uiState.weeks.isNotEmpty()) {
            watchRealIndex(page - (Int.MAX_VALUE / 2))
            Text(
                text = "Offset ${uiState.offsetIndex} \n" +
                        "Real Index ${page - (Int.MAX_VALUE / 2)} \n" +
                        "${uiState.weeks[computeIndex].map { "${it.dayOfWeek} ${it.dayOfMonth}" }}",
                modifier = modifier
            )
        }
    }
}


