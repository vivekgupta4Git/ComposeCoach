package com.ruviapps.coachmark.core

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.LayoutCoordinates

/**
 *@author Vivek Gupta on 13-11-23
 */


/**
 * A data class that encapsulates all the necessary information
 * to display a single coach mark or tutorial item.
 * It defines the position, content, and behavior of the coach mark.
 *
 * Each instance represents one highlight or guide element shown
 * during an onboarding or help flow.
 *
 * @property coordinates Provides the on-screen position and size
 * of the UI element that the coach mark highlights.
 *
 * @property content A composable lambda that defines what content
 * (text, icons, buttons, etc.) will be displayed inside the coach mark.
 *
 * @property revealEffect Defines how the highlight shape is drawn
 * and animated when the coach mark appears or disappears.
 * Implementations of [RevealEffect] control the enter and exit animations
 * and how the highlight shape around the target is visually rendered.
 *
 * @property alignment Specifies how the coach mark should be aligned
 * relative to the highlighted element (e.g., top, bottom, center).
 *
 * @property isForcedAlignment If true, the specified [alignment]
 * will always be used even if it might cause layout clipping
 * or visibility issues.
 *
 * @property coachStyle Determines the visual style and interaction behavior
 * of the coach mark. Implementations of [CoachStyle] control background
 * color, transparency, navigation button rendering, and the shape
 * of the highlight around the target element.
 */
data class CoachData(
    val coordinates: LayoutCoordinates,
    val content: @Composable BoxWithConstraintsScope.() -> Unit,
    val revealEffect: RevealEffect,
    val alignment: Alignment = Alignment.BottomCenter,
    val isForcedAlignment: Boolean = false,
    val coachStyle: CoachStyle,
    val isOutsideClickDismissable: Boolean = true,
)
