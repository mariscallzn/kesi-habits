package com.kesicollection.core.designsystem.component

import android.view.MotionEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kesicollection.core.designsystem.preview.DarkLightPreviews

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashedBox(
    dashesColor: Color,
    strokeWidth: Dp,
    dashLength: Dp,
    dashGap: Dp,
    phase: Dp = 0.dp,
    cornerRadius: CornerBasedShape = RoundedCornerShape(0.dp),
    contentAlignment: Alignment = Alignment.Center,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val gap by animateDpAsState(if (isPressed) 0.dp else dashGap, label = "gap")

    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier
            .clip(cornerRadius)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .then(modifier)
            .motionEventSpy { m ->
                if (m.action == MotionEvent.ACTION_DOWN && onClick != null) {
                    isPressed = true
                }

                if (m.action == MotionEvent.ACTION_UP && onClick != null) {
                    isPressed = false
                }
            }
            .drawBehind {
                drawRoundRect(
                    topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                    size = Size(
                        size.width - strokeWidth.toPx(),
                        size.height - strokeWidth.toPx()
                    ),
                    color = dashesColor,
                    cornerRadius = CornerRadius(
                        cornerRadius.topStart.toPx(size, Density(density)),
                        cornerRadius.topStart.toPx(size, Density(density))
                    ),
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(dashLength.toPx(), gap.toPx()),
                            phase = phase.toPx()
                        )
                    )
                )
            }

    ) {
        content()
    }
}

@DarkLightPreviews
@Composable
private fun DashedBoxPreview() {

}