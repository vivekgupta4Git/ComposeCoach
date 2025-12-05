package com.ruviapps.coachmark.core

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntSize

import kotlin.math.roundToInt
/**
 * A custom layout composable responsible for measuring and positioning the coach mark's
 * instructional message content relative to the target area.
 *
 * This layout implements logic to either force a specific alignment or automatically adjust
 * the content placement (above or below the target) to maximize available space and prevent clipping.
 *
 * @param canvasRect The [Rect] representing the full bounds of the coach mark scrim (the entire screen or host area).
 * @param targetBound The [Rect] representing the bounds of the composable being highlighted. The content will be positioned relative to this.
 * @param alignment The preferred [Alignment] for the content bubble relative to the target. Defaults to [Alignment.BottomCenter].
 * @param isForcedAlignment If true, the layout strictly adheres to the [alignment] using [findOffset], potentially allowing content to be clipped off-screen. If false (default), the layout intelligently chooses between the top and bottom half of the screen.
 * @param content The composable content (e.g., text, buttons) of the coach mark message.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
@Composable
fun CoachMessageLayout(
    canvasRect: Rect,
    targetBound: Rect,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    content: @Composable () -> Unit
) {
    Layout(
        content = content
    ) { measurables, constraints ->
        // Measure all children (supports multiple composables)
        val placeables = measurables.map { it.measure(constraints) }
        val contentWidth = placeables.maxOfOrNull { it.width } ?: 0
        val contentHeight = placeables.maxOfOrNull { it.height } ?: 0
        val contentSize = IntSize(contentWidth, contentHeight)

        // Calculate position
        val offset = if (isForcedAlignment) {
            findOffset(canvasRect, targetBound, contentSize, alignment)
        } else {
            calculateAdaptiveOffset(canvasRect, targetBound, contentSize)
        }

        // Layout size = canvas
        layout(canvasRect.width.roundToInt(), canvasRect.height.roundToInt()) {
            placeables.forEach { placeable ->
                placeable.placeRelative(
                    x = offset.x.roundToInt(),
                    y = offset.y.roundToInt()
                )
            }
        }
    }
}
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun calculateAdaptiveOffset(
    canvas: Rect,
    target: Rect,
    contentSize: IntSize
): Offset {
    val contentWidth = contentSize.width.toFloat()
    val contentHeight = contentSize.height.toFloat()
    val availableAbove = target.top
    val availableBelow = canvas.height - target.bottom

    val maxX = maxOf(0f, canvas.width - contentWidth)
    val centeredX = target.center.x - contentWidth / 2f
    val offsetX = centeredX.coerceIn(0f, maxX)

    val offsetY = if (availableAbove >= availableBelow) {
        // Prefer above
        if (contentHeight <= availableAbove) {
            // Fits above → place just above target
            target.top - contentHeight
        } else {
            // Too tall → align to top
            0f
        }
    } else {
        // Prefer below
        if (contentHeight <= availableBelow) {
            target.bottom
        } else {
            // Too tall → align to bottom, but ensure >=0
            maxOf(0f, canvas.height - contentHeight)
        }
    }

    // Final clamp for y to prevent negatives (though logic above should avoid)
    val maxY = maxOf(0f, canvas.height - contentHeight)
    val clampedY = offsetY.coerceIn(0f, maxY)

    return Offset(offsetX, clampedY)
}
/**
 * Calculates the [Offset] required to position a content message based on a fixed [Alignment]
 * relative to the target bounds.
 * @param canvas The Canvas available for drawing
 * @param targetBound The bounds of the highlighted target element.
 * @param contentSize The measured size of the coach mark content (message bubble).
 * @param alignment The desired fixed alignment (e.g., TopStart, BottomCenter).
 * @return The calculated [Offset] where the content should be placed relative to the layout's origin.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun findOffset(
    canvas: Rect,
    targetBound: Rect,
    contentSize: IntSize,
    alignment: Alignment
): Offset {
    val contentWidth = contentSize.width.toFloat()
    val contentHeight = contentSize.height.toFloat()
    val targetCenterX = targetBound.center.x
    val targetCenterY = targetBound.center.y

    val (alignX, alignY) = when (alignment) {
        Alignment.TopStart -> Offset(targetBound.left - contentWidth, targetBound.top - contentHeight)
        Alignment.TopCenter -> Offset(targetCenterX - contentWidth / 2f, targetBound.top - contentHeight)
        Alignment.TopEnd -> Offset(targetBound.right, targetBound.top - contentHeight)

        Alignment.CenterStart -> Offset(targetBound.left - contentWidth, targetCenterY - contentHeight / 2f)
        Alignment.Center -> Offset(targetCenterX - contentWidth / 2f, targetCenterY - contentHeight / 2f)
        Alignment.CenterEnd -> Offset(targetBound.right, targetCenterY - contentHeight / 2f)

        Alignment.BottomStart -> Offset(targetBound.left - contentWidth, targetBound.bottom)
        Alignment.BottomCenter -> Offset(targetCenterX - contentWidth / 2f, targetBound.bottom)
        Alignment.BottomEnd -> Offset(targetBound.right, targetBound.bottom)

        else -> Offset.Zero // fallback
    }

    // Clamp to canvas bounds, handling oversized content
    val maxX = maxOf(0f, canvas.width - contentWidth)
    val maxY = maxOf(0f, canvas.height - contentHeight)

    return Offset(
        x = alignX.coerceIn(0f, maxX),
        y = alignY.coerceIn(0f, maxY)
    )
}