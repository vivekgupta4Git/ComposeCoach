package com.ruviapps.coachmark.core

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.ruviapps.coachmark.DefaultCoachStyle
import com.ruviapps.coachmark.DefaultRevealEffect

@Stable
/**
 * A scope interface that provides access to the [CoachMarkState] and offers
 * utility functions to register composables as coach mark targets.
 *
 * This scope is typically used within a top-level composable that manages the coach mark display.
 */
interface CoachMarkScope {
    /**
     * The internal state manager for the coach mark system.
     *
     * This state holds all registered targets and manages the currently visible coach mark.
     */
    val state : CoachMarkState

    /**
     * Adds the composable this modifier is applied to as a coach mark target.
     *
     * The target's position and bounds are registered when the composable is first laid out
     * and anytime its global position changes (e.g., due to scrolling or layout changes).
     *
     * @param position A unique integer ID and order index for this coach mark target.
     * @param revealEffect The visual effect used to draw the "hole" revealing the target. Defaults to [DefaultRevealEffect].
     * @param backgroundCoachStyle The style used to draw the coach mark background (scrim and buttons). Defaults to [DefaultCoachStyle].
     * @param alignment The preferred alignment of the coach mark content relative to the target. Defaults to [Alignment.BottomCenter].
     * @param isForcedAlignment If true, the coach mark content will strictly adhere to the [alignment] even if it causes clipping.
     * @param isOutsideClickDismissable If true, tapping on the scrim outside the target area will dismiss the coach mark and move to the next step.
     * @param content The composable content to display within the coach mark overlay when this target is active.
     * @return A [Modifier] that tracks the composable's position and registers it as a coach mark target.
     */
    fun Modifier.addTarget(
        position : Int,
        revealEffect: RevealEffect = DefaultRevealEffect,
        backgroundCoachStyle: CoachStyle = DefaultCoachStyle,
        alignment: Alignment = Alignment.BottomCenter,
        isForcedAlignment : Boolean = false,
        isOutsideClickDismissable: Boolean = true,
        content : @Composable BoxWithConstraintsScope.() -> Unit,
    ) = onGloballyPositioned {
            layoutCoordinates ->
        state.addTarget(position , CoachData(
            content = content,
            coordinates = layoutCoordinates,
            revealEffect = revealEffect,
            alignment = alignment,
            isForcedAlignment = isForcedAlignment,
            coachStyle = backgroundCoachStyle,
            isOutsideClickDismissable = isOutsideClickDismissable
        )
        )
    }
}