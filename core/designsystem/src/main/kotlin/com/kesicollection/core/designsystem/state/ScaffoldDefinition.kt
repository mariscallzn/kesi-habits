package com.kesicollection.core.designsystem.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberScaffoldDefinition(): ScaffoldDefinitionState = remember {
    ScaffoldDefinitionState()
}

class ScaffoldDefinitionState {

    var appBarComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var fabComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    var bottomComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    fun defineAppBarComposable(content: @Composable () -> Unit) {
        appBarComposable = content
    }

    fun defineFabComposable(content: (@Composable () -> Unit)?) {
        fabComposable = content
    }

    fun defineBottomComposable(content: (@Composable () -> Unit)?) {
        bottomComposable = content
    }

}