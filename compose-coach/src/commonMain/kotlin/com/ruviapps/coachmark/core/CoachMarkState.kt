package com.ruviapps.coachmark.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * A stable state holder class responsible for managing the entire coach mark tour sequence.
 *
 * This class uses Compose's state primitives to ensure that the CoachMarkHost composable automatically
 * reacts to changes in the current step ([currentCoach]) or when targets are added.
 * The class optimizes navigation by sorting keys only on demand via the [sortedKeys] property.
 *
 * @property startPosition The initial position index for the tour. This is the index the tour starts
 * from and resets to if no targets are registered. Defaults to 1.
 *
 * @property eventListener The event listener for the tour. Default provides empty event listener.
 */
@Stable
class CoachMarkState(
    private val startPosition: Int = 1,
    val eventListener: CoachMarkEventListener = EmptyCoachMarkEventListener(),
) {

    /**
     * The observable map of all registered coach mark targets.
     *
     * Keys represent the tour position (order), and values hold the [CoachData] required
     * to draw and position the coach mark.
     */
    internal val targets = mutableStateMapOf<Int, CoachData>()

    /**
     * A computed list of all target keys, sorted numerically.
     *
     * This property is computed dynamically every time it is accessed to ensure the latest
     * state of the [targets] map is reflected in the navigation order.
     */
    private val sortedKeys: List<Int>
        get() = targets.keys.sorted()

    /**
     * The observable integer index representing the position of the currently active coach mark in the tour.
     *
     * This state drives which target's data is reflected in [currentCoach].
     */
    var currentKeyPosition by mutableIntStateOf(startPosition)
        private set

    /**
     * The observable data class containing all information needed to display the current coach mark (target coordinates, content, styles, etc.).
     *
     * This is the primary state used by the [Coach] composable to render the overlay. It is null
     * when the tour is hidden.
     */
    var currentCoach by mutableStateOf<CoachData?>(null)
        private set

    /**
     * DERIVED STATE: True if the tour has been completed or manually hidden.
     * This relies on [hideCoach] setting the position to -1.
     */
    val hasCompleted: Boolean
        get() = currentKeyPosition == -1

    /**
     * Registers a new coach mark target with its associated data.
     *
     * If the tour has not been started ([currentCoach] is null), this function will automatically
     * set the current coach to the one corresponding to [currentKeyPosition].
     *
     * @param position The position index (key) where the target will be placed in the tour order.
     * @param coachData The data (content, coordinates, styles) for the new target.
     */
    fun addTarget(position: Int, coachData: CoachData) {
        targets[position] = coachData
        // Initialize current coach if the tour hasn't started yet (currentCoach == null).
        if (currentCoach == null)
            currentCoach = targets[currentKeyPosition]
    }

    /**
     * Hides the coach mark overlay by setting the position to an invalid index (-1) and clearing the current coach data.
     */
    fun hideCoach() {
        eventListener.onSkipCalled()
        currentKeyPosition = -1
        currentCoach = null
    }

    /**
     * Advances the tour to the next sequential coach mark based on the sorted keys.
     *
     * If the current step is the last one, the tour is hidden via [hideCoach].
     */
    fun next() {

        val currentPos = sortedKeys.indexOf(currentKeyPosition)
        if (currentPos >= 0 && currentPos < sortedKeys.size - 1) {
            eventListener.onNextCalled()
            currentKeyPosition = sortedKeys[currentPos + 1]
            currentCoach = targets[currentKeyPosition]
        } else {
            currentKeyPosition = -1
            currentCoach = null
        }
    }

    /**
     * Moves the tour to the previous sequential coach mark based on the sorted keys.
     *
     * If the current step is the first one, the tour is hidden via [hideCoach].
     */
    fun previous() {
        val currentPos = sortedKeys.indexOf(currentKeyPosition)
        if (currentPos > 0) {
            eventListener.onPreviousCalled()
            currentKeyPosition = sortedKeys[currentPos - 1]
            currentCoach = targets[currentKeyPosition]
        } else {
            currentKeyPosition = -1
            currentCoach = null
        }

    }

    /**
     * Resets the tour to the very first registered target (the smallest key).
     *
     * If no targets are present, it resets the position to the [startPosition].
     */
    fun reset() {
        currentKeyPosition = targets.keys.minOrNull() ?: startPosition
        currentCoach = targets[currentKeyPosition]
    }

    /**
     * Restores the coach mark's progression state by setting the current key position
     * to a previously saved value.
     *
     * This method is intended to be used by the state Saver during process recreation
     * (e.g., configuration changes or process death) to bring the coach mark tour back
     * to the correct step. It does **not** attempt to restore `currentCoach` directly,
     * as coach data depends on runtime-computed targets; once targets are re-registered
     * through `addTarget()`, the appropriate `currentCoach` will be recalculated
     * automatically.
     *
     * @param keyPosition The saved key position to restore.
     */
    fun restoreKey(keyPosition: Int) {
        currentKeyPosition = keyPosition
    }

}

/**
 * Creates and remembers a [CoachMarkState] instance, ensuring the same instance
 * is used across recompositions.
 *
 * @param startPosition The initial index to begin the coach mark tour from.
 * @param listener   An interface providing callbacks for key lifecycle and navigation events in the tour,
 * such as when a step is completed, skipped, or the entire tour finishes. Defaults to a no-op implementation.
 * @return A remembered instance of [CoachMarkState].
 */
@Composable
fun rememberCoachMarkState(
    startPosition: Int = 1,
    listener: CoachMarkEventListener = EmptyCoachMarkEventListener()
): CoachMarkState {

    val saver = remember {
        Saver<CoachMarkState, Int>(
            save = { state ->
                // Save only the currentKeyPosition
                state.currentKeyPosition
            },
            restore = { savedKeyPosition ->
                CoachMarkState(startPosition, listener).apply {
                    restoreKey(keyPosition = savedKeyPosition)
                }
            }
        )
    }

    return rememberSaveable(
        saver = saver
    ) {
        CoachMarkState(startPosition, listener)
    }
}
