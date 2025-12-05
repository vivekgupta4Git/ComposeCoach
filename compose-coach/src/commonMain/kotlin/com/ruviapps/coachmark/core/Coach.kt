package com.ruviapps.coachmark.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Surface
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
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import com.ruviapps.coachmark.DefaultCoachStyle
import com.ruviapps.coachmark.DefaultRevealEffect
import kotlinx.coroutines.launch

/**
 * A highly customizable composable that displays a single coach mark overlay, highlighting a
 * specific target element on the screen and presenting associated instructional content.
 *
 * This component handles the drawing of the transparent scrim, the reveal effect over the target,
 * input dismissal logic, and the layout of the instructional content and navigation buttons.
 *
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
 * will also dismiss the Coach.
 * @param state The state object that manages the coach mark targets and the current step in the tour.
 * Defaults to a remembered [CoachMarkState]. Use this parameter to control the tour externally.
 * such as when a step is completed, skipped, or the entire tour finishes. Defaults to a no-op implementation.
  */
@Composable
internal fun Coach(
    coordinates: LayoutCoordinates,
    content: @Composable BoxWithConstraintsScope.() -> Unit,
    coachStyle: CoachStyle,
    revealEffect: RevealEffect,
    alignment: Alignment,
    isForcedAlignment: Boolean,
    isOutsideClickDismissable: Boolean,
    state: CoachMarkState,
) {
    val bounds = coordinates.boundsInRoot()
    val scope = rememberCoroutineScope()
    // Target Bounds for the tap area
    var targetBoundary by remember { mutableStateOf(Rect.Zero) }
    // Coach Shape Bounds (the whole scrim)
    var scrimBoundary by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(key1 = bounds) {
        revealEffect.enterAnimation(bounds)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                compositingStrategy = CompositingStrategy.Offscreen
            ),
        color = Color.Transparent
    )
    {
        Canvas(
            modifier = Modifier
                // This ensures the pointerInput block restarts and uses the LATEST bounds.
                .pointerInput(targetBoundary) {
                    detectTapGestures { offset ->
                        if (isOutsideClickDismissable || targetBoundary.contains(offset))
                            scope.launch {
                                revealEffect.exitAnimation(bounds)
                                state.next()
                            }
                    }
                },
            onDraw = {
                if(state.hasCompleted.not()){
                    scrimBoundary = coachStyle.drawCoachShape(bounds, this)
                    targetBoundary = revealEffect.drawTargetShape(bounds, this)
                }
            })
        if(state.hasCompleted.not()){
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
                            state.hideCoach()
                        }
                    },
                    onNext = {  scope.launch {
                        revealEffect.exitAnimation(bounds)
                        state.next()
                    } },
                    onBack = {
                        scope.launch {
                            revealEffect.exitAnimation(bounds)
                            state.previous()
                        }

                    },
                    targetBounds = bounds
                )
                CoachMessageLayout(
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
}

