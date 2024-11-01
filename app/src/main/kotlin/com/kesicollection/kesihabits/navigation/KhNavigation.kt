package com.kesicollection.kesihabits.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kesicollection.core.designsystem.state.ScaffoldDefinitionState
import com.kesicollection.feature.addentry.navigation.AddEntry
import com.kesicollection.feature.addentry.navigation.addEntryScreen
import com.kesicollection.feature.addentry.navigation.navigateToAddEntry
import com.kesicollection.feature.createemotion.navigation.CreateEmotion
import com.kesicollection.feature.createemotion.navigation.createEmotionScreen
import com.kesicollection.feature.createemotion.navigation.navigateToCreateEmotion
import com.kesicollection.feature.createhabit.navigation.CreateHabit
import com.kesicollection.feature.createhabit.navigation.createHabitScreen
import com.kesicollection.feature.createhabit.navigation.navigateToCreateHabit
import com.kesicollection.feature.emotionpicker.navigation.EmotionPicker
import com.kesicollection.feature.emotionpicker.navigation.emotionPickerScreen
import com.kesicollection.feature.emotionpicker.navigation.navigateToEmotionPicker
import com.kesicollection.feature.habitpicker.navigation.HabitPicker
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
            addEntryClick = { navController.navigateToAddEntry(AddEntry()) },
            modifier = modifier
        )
        addEntryScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onAddHabitClick = { entryDraftId, type ->
                navController.navigateToHabitPicker(
                    HabitPicker(type, entryDraftId)
                )
            },
            onAddEmotionClick = { entryDraftId, emotionIds, type ->
                navController.navigateToEmotionPicker(EmotionPicker(entryDraftId, type, emotionIds))
            },
            modifier = modifier
        )
        habitPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onCreateHabitClick = { entryDraftId, type ->
                navController.navigateToCreateHabit(
                    CreateHabit(type, entryDraftId)
                )
            },
            modifier = modifier,
        )
        createHabitScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onHabitCreated = { entryDraftId, habitId, type ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(AddEntry(entryDraftId, habitId, type))
            },
            modifier = modifier,
        )
        emotionPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onCreateEmotionClick = { entryDraftId, emotionType ->
                navController.navigateToCreateEmotion(CreateEmotion(entryDraftId, emotionType))
            },
            onEmotionsSelected = { entryDraftId, emotionIds, type ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(
                    AddEntry(
                        draftId = entryDraftId,
                        emotionIds = emotionIds,
                        emotionType = type
                    )
                )
            },
            modifier = modifier,
        )
        createEmotionScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onCreateEmotionClick = { entryDraftId, emotionId, type ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(
                    AddEntry(
                        draftId = entryDraftId,
                        emotionIds = listOf(emotionId),
                        emotionType = type
                    )
                )
            },
            modifier = modifier,
        )
    }
}