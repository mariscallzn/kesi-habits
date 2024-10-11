package com.kesicollection.feature.weeklyhabits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    Column {
        Text(text = uiState.status)
        Button({
            viewModel.dispatch(ScreenAction.One)
        }) {
            Text("Sync")
        }
        Button({
            viewModel.dispatch(viewModel.test("These are the thunk args"))
        }) {
            Text("Async")
        }
        Button({
            viewModel.dispatch(viewModel.test("Boom"))
        }) {
            Text("Async with Exception")
        }
    }


//    WeeklyHabitsScreen(uiState, setAppBarTitle, setFabOnClick, {}, modifier)
}

/**
 * Stateless
 */
@Composable
internal fun WeeklyHabitsScreen(
    uiState: WeeklyHabitsUiState,
    setAppBarTitle: (String?) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    setPagerIndex: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(
            pageCount = { Int.MAX_VALUE },
            initialPage = (Int.MAX_VALUE / 2)
        )


    HorizontalPager(state = pagerState) { page ->
        val computeIndex = page - (Int.MAX_VALUE / 2)
        setPagerIndex(computeIndex)
        Text(
            text = "computed index $computeIndex counter $uiState",
            modifier = modifier
        )
    }
}


