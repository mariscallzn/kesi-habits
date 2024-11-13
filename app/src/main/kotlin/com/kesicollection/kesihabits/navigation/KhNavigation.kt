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
import com.kesicollection.feature.createinfluencer.navigation.CreateInfluencer
import com.kesicollection.feature.createinfluencer.navigation.createInfluencerScreen
import com.kesicollection.feature.createinfluencer.navigation.navigateToCreateInfluencer
import com.kesicollection.feature.emotionpicker.navigation.EmotionPicker
import com.kesicollection.feature.emotionpicker.navigation.emotionPickerScreen
import com.kesicollection.feature.emotionpicker.navigation.navigateToEmotionPicker
import com.kesicollection.feature.habitpicker.navigation.HabitPicker
import com.kesicollection.feature.habitpicker.navigation.habitPickerScreen
import com.kesicollection.feature.habitpicker.navigation.navigateToHabitPicker
import com.kesicollection.feature.weeklyhabits.navigation.WeeklyHabits
import com.kesicollection.feature.weeklyhabits.navigation.navigateToWeeklyHabits
import com.kesicollection.feature.weeklyhabits.navigation.weeklyHabitsScreen
import com.kesicollection.influencerpicker.navigation.InfluencerPicker
import com.kesicollection.influencerpicker.navigation.influencerPickerScreen
import com.kesicollection.influencerpicker.navigation.navigateToInfluencerPicker

@Composable
fun KhNavHost(
    scaffoldDefinitionState: ScaffoldDefinitionState,
    modifier: Modifier = Modifier
) {
    val startDestination = WeeklyHabits()
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
            onEntryCreated = { recordedOn ->
                navController.popBackStack<WeeklyHabits>(true)
                navController.navigateToWeeklyHabits(WeeklyHabits(recordedOn))
            },
            onAddHabitClick = { entryDraftId, habitId, type ->
                navController.navigateToHabitPicker(
                    HabitPicker(type, entryDraftId, habitId)
                )
            },
            onAddEmotionClick = { entryDraftId, emotionIds, type ->
                navController.navigateToEmotionPicker(EmotionPicker(entryDraftId, type, emotionIds))
            },
            onAddInfluencerClick = { entryDraftId, influencerIds ->
                navController.navigateToInfluencerPicker(
                    InfluencerPicker(entryDraftId, influencerIds)
                )
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
            onHabitSelected = { entryDraftId, habitId, type ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(AddEntry(entryDraftId, habitId, type))
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
                //TODO: I have to carry the previously selected ids otherwise I'll remove them and just assign the created one only
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
        influencerPickerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onCreateInfluencerClick = { entryDraftId ->
                navController.navigateToCreateInfluencer(CreateInfluencer(entryDraftId))
            },
            onInfluencersSelected = { entryDraftId, ids ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(
                    AddEntry(
                        draftId = entryDraftId,
                        influencerIds = ids,
                    )
                )
            },
            modifier = modifier,
        )
        createInfluencerScreen(
            scaffoldDefinitionState = scaffoldDefinitionState,
            onBackPressed = navController::popBackStack,
            onCreateInfluencerClick = { entryDraftId, createdInfluencerId ->
                navController.popBackStack<AddEntry>(true)
                navController.navigateToAddEntry(
                    AddEntry(
                        draftId = entryDraftId,
                        influencerIds = listOf(createdInfluencerId),
                    )
                )
            },
            modifier = modifier,
        )
    }
}