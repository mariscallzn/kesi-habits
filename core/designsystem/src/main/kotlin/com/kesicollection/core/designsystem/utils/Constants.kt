package com.kesicollection.core.designsystem.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

const val TAG = "Andres"

@Composable
fun tertiaryButtonColor(): ButtonColors {
    return ButtonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun statusBarPaddingValues(): PaddingValues =
    WindowInsets.statusBars
//        .add(WindowInsets.safeContent.only(WindowInsetsSides.Top))
        .asPaddingValues()
