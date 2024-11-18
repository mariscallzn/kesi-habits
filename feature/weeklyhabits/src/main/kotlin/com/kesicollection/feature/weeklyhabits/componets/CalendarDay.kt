package com.kesicollection.feature.weeklyhabits.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.core.model.Day

@Composable
fun CalendarDay(
    onClick: (Day) -> Unit = {},
    isSelected: Boolean = false,
    isCurrentDay: Boolean = false,
    day: Day,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.shapes.large.topStart))
            .clickable { onClick(day) }
            .width(48.dp),
        shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
        color = if (isSelected) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    ) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                day.dayOfWeek,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(MaterialTheme.shapes.small.topStart)
                    ),
            ) {
                Text(
                    day.dayOfMonth, modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp)
                )
                if (isCurrentDay) {
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(16.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(
                                    4.dp, 4.dp
                                )
                            )
                    )
                }
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun CalendarDayPreviewSelected() {
    KesiTheme(dynamicColor = false) {
        CalendarDay(day = Day("Tue", "12", ""), isSelected = true, isCurrentDay = true)
    }

}

@DarkLightPreviews
@Composable
private fun CalendarDayPreviewUnSelected() {
    KesiTheme(dynamicColor = false) {
        CalendarDay(day = Day("Tue", "12", ""), isSelected = false, isCurrentDay = true)
    }
}