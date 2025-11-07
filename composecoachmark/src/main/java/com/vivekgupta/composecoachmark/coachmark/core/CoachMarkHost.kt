package com.vivekgupta.composecoachmark.coachmark.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * The main container composable for the coach mark feature.
 *
 * This composable initializes the [CoachMarkState], provides the [CoachMarkScope] for registering
 * targets within its content, and conditionally displays the currently active [Coach] overlay.
 *
 * @param showCoach Controls the visibility of the entire coach mark system. Set to `false` to hide
 * the overlay, effectively pausing the tour. Defaults to `true`.
 * @param state The state object that manages the coach mark targets and the current step in the tour.
 * Defaults to a remembered [CoachMarkState]. Use this parameter to control the tour externally.
 * @param content The primary content of the screen. Within this lambda, use the
 * [CoachMarkScope.addTarget] modifiers on composables
 * that should be highlighted as targets in the tour.
 */
@Composable
fun CoachMarkHost(
    showCoach: Boolean = true,
    state: CoachMarkState = rememberCoachMarkState(),
    content: @Composable CoachMarkScope.() -> Unit
) {
    val scopeImpl = remember {
        object : CoachMarkScope {
            override val state: CoachMarkState
                get() = state
        }
    }
    scopeImpl.content()


    val hasCompleted by remember {
        derivedStateOf { state.hasCompleted }
    }
    LaunchedEffect(hasCompleted) {
        if (hasCompleted) {
            state.eventListener.onComplete()
        }
    }
    if (showCoach) {
        state.currentCoach?.let { coach ->
            Coach(
                coordinates = coach.coordinates,
                content = coach.content,
                coachStyle = coach.coachStyle,
                revealEffect = coach.revealEffect,
                alignment = coach.alignment,
                isForcedAlignment = coach.isForcedAlignment,
                state = state,
                isOutsideClickDismissable = coach.isOutsideClickDismissable
            )
        }
    }

}
