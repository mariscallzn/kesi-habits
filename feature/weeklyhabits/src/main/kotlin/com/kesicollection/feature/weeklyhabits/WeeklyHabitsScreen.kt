package com.kesicollection.feature.weeklyhabits

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.core.model.Day
import com.kesicollection.feature.weeklyhabits.componets.CalendarDay
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

    val locale = LocalConfiguration.current.locales[0]
    LaunchedEffect(locale) {
        viewModel.dispatch(ScreenAction.LoadCalendar(locale = locale))
    }

    LaunchedEffect(Unit) {
        Log.d("Andres", "WeeklyHabitsScreen: $weeklyHabits")
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

    WeeklyHabitsScreen(
        uiState = uiState,
        onDaySelected = { day -> viewModel.dispatch(ScreenAction.SelectDay(day)) },
        watchRealIndex = { i ->
            viewModel.dispatch(viewModel.watchPagingIndex(i))
        }, modifier = modifier
    )
}

/**
 * Stateless
 */
@Composable
internal fun WeeklyHabitsScreen(
    uiState: WeeklyHabitsUiState,
    onDaySelected: (Day) -> Unit,
    watchRealIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val pagerState =
        rememberPagerState(
            pageCount = { Int.MAX_VALUE },
            initialPage = (Int.MAX_VALUE / 2)
        )

    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        val computeIndex = uiState.offsetIndex + (page - (Int.MAX_VALUE / 2))
        if (uiState.weeks.isNotEmpty()) {
            watchRealIndex(page - (Int.MAX_VALUE / 2))
            Column(modifier = Modifier.fillMaxHeight()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    uiState.weeks[computeIndex].map {
                        CalendarDay(
                            day = it,
                            isSelected = it == uiState.selectedDay,
                            isCurrentDay = it == uiState.currentDay,
                            onClick = { d -> onDaySelected(d) }
                        )
                    }
                }
            }
        }
    }
}