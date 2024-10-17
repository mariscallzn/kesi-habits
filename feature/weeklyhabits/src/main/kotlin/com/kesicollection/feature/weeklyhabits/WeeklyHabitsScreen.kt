package com.kesicollection.feature.weeklyhabits

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * State full
 */
@Composable
internal fun WeeklyHabitsScreen(
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeeklyHabitsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatch(ScreenAction.LoadCalendar())
    }

    WeeklyHabitsScreen(uiState, setAppBarTitle, setFabOnClick, { i ->
        viewModel.dispatch(viewModel.watchPagingIndex(i))
    }, modifier)
}

/**
 * Stateless
 */
@Composable
internal fun WeeklyHabitsScreen(
    uiState: WeeklyHabitsUiState,
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
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


