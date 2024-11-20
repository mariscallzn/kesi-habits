package com.kesicollection.feature.weeklyhabits.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.core.model.Classification
import com.kesicollection.feature.weeklyhabits.model.EntryItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntryItem(
    item: EntryItem,
    modifier: Modifier = Modifier
) {
    val classification: Pair<Color, ImageVector> = when (item.classification) {
        Classification.POSITIVE -> MaterialTheme.colorScheme.tertiary to KesiIcons.AddCircle
        Classification.NEGATIVE -> MaterialTheme.colorScheme.error to KesiIcons.Cancel
        Classification.NEUTRAL -> MaterialTheme.colorScheme.secondary to KesiIcons.RemoveCircle
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            item.time,
            maxLines = 1,
            modifier= Modifier.weight(0.3f),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleSmall
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(item.title)
            FlowRow {
                item.humanNeeds.mapIndexed { index, s ->
                    Text(
                        "$s${if (index != item.humanNeeds.size - 1) " • " else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        Icon(classification.second, "", tint = classification.first)
    }
}

@DarkLightPreviews
@Composable
private fun EntryItemNegativePreview() {
    KesiTheme(dynamicColor = false) {
        EntryItem(
            item = EntryItem(
                "Smoking",
                "10:01 AM",
                Classification.NEGATIVE,
                listOf(
                    "Certainty",
                    "Uncertainty",
                    "Significance",
                    "Connection / Love",
                    "Growth",
                    "Contribution"
                )
            )
        )
    }
}

@DarkLightPreviews
@Composable
private fun EntryItemNeutralPreview() {
    KesiTheme(dynamicColor = false) {
        EntryItem(
            item = EntryItem(
                "Relaxing on work chair",
                "9:01 AM",
                Classification.NEUTRAL,
                listOf()
            )
        )
    }
}

@DarkLightPreviews
@Composable
private fun EntryItemPositivePreview() {
    KesiTheme(dynamicColor = false) {
        EntryItem(
            item = EntryItem(
                "Study",
                "9:01 AM",
                Classification.POSITIVE,
                listOf(
                    "Certainty",
                    "Uncertainty",
                    "Significance",
                    "Connection / Love",
                    "Growth",
                    "Contribution"
                )
            )
        )
    }
}

@DarkLightPreviews
@Composable
private fun EntryItemNeutralLongTextNoHumanNeedsPreview() {
    KesiTheme(dynamicColor = false) {
        EntryItem(
            item = EntryItem(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "9:01 AM",
                Classification.NEUTRAL,
                listOf(
                    "Certainty",
                    "Uncertainty",
                    "Significance",
                    "Connection / Love",
                    "Growth",
                    "Contribution"
                )
            )
        )
    }
}

@DarkLightPreviews
@Composable
private fun EntryItemNeutralLongTextPreview() {
    KesiTheme(dynamicColor = false) {
        EntryItem(
            item = EntryItem(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                "9:01 AM",
                Classification.NEUTRAL,
                listOf()
            )
        )
    }
}