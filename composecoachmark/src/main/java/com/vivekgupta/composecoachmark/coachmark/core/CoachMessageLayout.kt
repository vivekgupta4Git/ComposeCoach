package com.vivekgupta.composecoachmark.coachmark.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.SubcomposeLayout
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
@Composable
fun CoachMessageLayout(
    canvasRect: Rect,
    targetBound: Rect,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    content: @Composable () -> Unit
) {

    SubcomposeLayout { constraints ->
        // Measure the content (the actual message/bubble)
        val mainPlaceable = subcompose(Unit, content).map {
            it.measure(constraints)
        }
        // Determine the actual size of the content bubble
        val contentSize = mainPlaceable.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }
        // Determine the placement offset based on alignment
        val offset = if (isForcedAlignment) {
            findOffset(targetBound, contentSize, alignment)
        } else {
            // Adaptive placement logic: choose the largest available space (top or bottom)

            // Available space above the target
            val upperRectangle = Rect(
                top = 0f,
                bottom = targetBound.topRight.y,
                right = canvasRect.right,
                left = targetBound.left
            )
            // Available space below the target
            val bottomRectangle = Rect(
                top = targetBound.bottomLeft.y,
                bottom = canvasRect.bottom,
                right = canvasRect.right,
                left = canvasRect.left
            )
            // If the top space is larger or equal
            if (upperRectangle.height >= bottomRectangle.height) {
                // If the content fits in the top space, position it just above the target
                if (contentSize.height <= upperRectangle.height)
                    Offset(0f, targetBound.top - contentSize.height)
                else
                // If content doesn't fit, start it from the top edge of the screen
                    upperRectangle.topLeft
            } else {
                // Use the bottom space
                // Start positioning from the bottom edge of the target
                bottomRectangle.topLeft
            }
        }

        layout(width = canvasRect.width.roundToInt(), canvasRect.height.roundToInt()) {
            mainPlaceable.forEach {
                // Place content relative to the canvas origin (0, 0) using the calculated y-offset.
                // Assuming x-placement is handled by the content itself or the parent layout.
                it.placeRelative(0, offset.y.toInt())
            }
        }
    }
}

/**
 * Calculates the [Offset] required to position a content message based on a fixed [Alignment]
 * relative to the target bounds.
 *
 * @param targetBound The bounds of the highlighted target element.
 * @param contentSize The measured size of the coach mark content (message bubble).
 * @param alignment The desired fixed alignment (e.g., TopStart, BottomCenter).
 * @return The calculated [Offset] where the content should be placed relative to the layout's origin.
 */
private fun findOffset(
    targetBound: Rect,
    contentSize: IntSize,
    alignment: Alignment,
): Offset {
    val contentWidth = contentSize.width.toFloat()
    val contentHeight = contentSize.height.toFloat()

    return when (alignment) {
        // Positions content to the Top-Left of the target
        Alignment.TopStart -> {
            Offset(
                x = targetBound.topLeft.x - contentWidth,
                y = targetBound.topLeft.y - contentHeight
            )
        }

        // Centers content horizontally and positions it above the target
        Alignment.TopCenter -> {
            Offset(
                x = targetBound.center.x - contentWidth / 2f,
                y = targetBound.top - contentHeight
            )
        }

        // Positions content to the Top-Right of the target
        Alignment.TopEnd -> {
            Offset(
                x = targetBound.topRight.x,
                y = targetBound.topRight.y - contentHeight
            )
        }

        // Centers content vertically and positions it to the Left of the target
        Alignment.CenterStart -> {
            Offset(
                x = targetBound.left - contentWidth,
                y = targetBound.center.y - contentHeight / 2f,
            )
        }

        // Centers content vertically and positions it to the Right of the target
        Alignment.CenterEnd -> {
            Offset(
                x = targetBound.right,
                y = targetBound.center.y - contentHeight / 2f,
            )
        }

        // Centers content completely over the target
        Alignment.Center -> {
            Offset(
                x = targetBound.center.x - contentWidth / 2f,
                y = targetBound.center.y - contentHeight / 2f
            )
        }

        // Positions content to the Bottom-Left of the target
        Alignment.BottomStart -> {
            Offset(
                x = targetBound.bottomLeft.x - contentWidth,
                y = targetBound.bottomLeft.y,
            )
        }

        // Centers content horizontally and positions it below the target
        Alignment.BottomCenter -> {
            Offset(
                x = targetBound.center.x - contentWidth / 2f,
                y = targetBound.bottom
            )
        }

        // Positions content to the Bottom-Right of the target
        Alignment.BottomEnd -> {
            Offset(
                x = targetBound.bottomRight.x,
                y = targetBound.bottomRight.y,
            )
        }

        // Fallback alignment (should not be reached with standard Alignment values)
        else -> {
            Offset(0f, 0f)
        }
    }

}