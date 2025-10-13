package com.vivekgupta.composecoachmark.coachmark.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import kotlinx.coroutines.launch
import androidx.compose.material3.Surface
import com.vivekgupta.composecoachmark.coachmark.DefaultCoachStyle
import com.vivekgupta.composecoachmark.coachmark.DefaultRevealEffect

/**
 * A highly customizable composable that displays a single coach mark overlay, highlighting a
 * specific target element on the screen and presenting associated instructional content.
 *
 * This component handles the drawing of the transparent scrim, the reveal effect over the target,
 * input dismissal logic, and the layout of the instructional content and navigation buttons.
 *
 * @param modifier The modifier to be applied to the top-level Surface containing the coach mark.
 * @param coordinates The [LayoutCoordinates] of the target composable element being highlighted,
 * used to determine the target's position and bounds within the screen.
 * @param content The composable lambda defining the instructional content (text, image, etc.)
 * to be displayed in the coach mark's overlay bubble.
 * @param coachStyle The styling component that draws the background scrim, buttons, and handles
 * the layout of content and controls. Defaults to [DefaultCoachStyle].
 * @param revealEffect The drawing component responsible for creating the "hole" or visual effect
 * over the target element. Defaults to [DefaultRevealEffect].
 * @param alignment The preferred alignment of the coach mark content bubble relative to the target area.
 * Defaults to [Alignment.BottomCenter].
 * @param isForcedAlignment If true, the coach mark content will strictly adhere to the [alignment]
 * even if it risks clipping. If false, the layout may adjust the alignment
 * to keep the content on screen.
 * @param isOutsideClickDismissable If true, tapping anywhere on the scrim *outside* the target area
 * will trigger the [onNext] action (dismissal).
 * @param onBack Callback invoked when the 'Back' button is pressed.
 * @param onSkip Callback invoked when the 'Skip' button is pressed.
 * @param onNext Callback invoked when the 'Next' button is pressed or the coach mark is dismissed.
 */
@Composable
internal fun Coach(
    modifier: Modifier = Modifier,
    coordinates: LayoutCoordinates,
    content: @Composable BoxWithConstraintsScope.() -> Unit,
    coachStyle: CoachStyle = DefaultCoachStyle,
    revealEffect: RevealEffect = DefaultRevealEffect,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    isOutsideClickDismissable: Boolean = true,
    onBack: () -> Unit = {},
    onSkip: () -> Unit = {},
    onNext: () -> Unit,
) {
    val bounds = coordinates.boundsInRoot()
    LaunchedEffect(key1 = bounds) {
        revealEffect.enterAnimation(bounds)
    }
    val scope = rememberCoroutineScope()
    // Target Bounds for the tap area
    var targetBoundary by remember { mutableStateOf(Rect.Zero) }
    // Coach Shape Bounds (the whole scrim)
    var scrimBoundary by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(key1 = bounds) {
        revealEffect.enterAnimation(bounds)
    }

    // Common action for dismissal/next
    val handleDismiss: () -> Unit = {
        scope.launch {
            revealEffect.exitAnimation(bounds)
            onNext()
        }
    }
    Surface(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
            .pointerInput(bounds) {
                detectTapGestures { offset ->
                    if (isOutsideClickDismissable) {
                        // Check if tap is OUTSIDE the newBound/target area
                        if (!targetBoundary.contains(offset)) {
                            handleDismiss()
                        }
                    }
                }
            },
        color = Color.Transparent
    )
    {
        Canvas(
            modifier = Modifier
                // This ensures the pointerInput block restarts and uses the LATEST bounds.
                .pointerInput(targetBoundary) {
                    detectTapGestures { offset ->
                        if (targetBoundary.contains(offset)) {
                            handleDismiss()
                        }
                    }
                },
            onDraw = {
                scrimBoundary = coachStyle.drawCoachShape(bounds, this)
                targetBoundary = revealEffect.drawTargetShape(bounds, this)
            })
        BoxWithConstraints(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            coachStyle.drawCoachButtons(
                contentScope = this,
                onSkip = {
                    scope.launch {
                        revealEffect.exitAnimation(bounds)
                        onSkip()
                    }
                },
                onNext = {
                    scope.launch {
                        revealEffect.exitAnimation(bounds)
                        onNext()
                    }
                },
                onBack = {
                    scope.launch {
                        revealEffect.exitAnimation(bounds)
                        onBack()
                    }

                },
                targetBounds = bounds
            )
            NewCoachLayout(
                canvasRect = scrimBoundary,
                targetBound = targetBoundary,
                alignment = alignment,
                isForcedAlignment = isForcedAlignment
            ) {
                content()
            }
        }
    }
}

