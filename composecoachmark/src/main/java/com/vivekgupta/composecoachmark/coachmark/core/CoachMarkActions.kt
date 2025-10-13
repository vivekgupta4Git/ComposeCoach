package com.vivekgupta.composecoachmark.coachmark.core


/**
 * An interface defining external callbacks for lifecycle and navigation events within the coach mark tour.
 *
 * This allows external controllers (e.g., ViewModels or higher-level composables) to react
 * to user navigation and the completion of the entire tour sequence.
 */
interface CoachMarkActions {
    /**
     * Called when the user explicitly triggers navigation to the previous coach mark step.
     *
     * This is typically invoked when the 'Back' button is pressed.
     */
    fun onPreviousCalled()

    /**
     * Called when the user explicitly triggers navigation to the next coach mark step.
     *
     * This is typically invoked when the 'Next' button is pressed or an outside-click dismissal occurs.
     */
    fun onNextCalled()

    /**
     * Called when the user explicitly skips the entire coach mark tour.
     *
     * This is typically invoked when the 'Skip' button is pressed.
     */
    fun onSkipCalled()

    /**
     * Called only once when the coach mark tour successfully finishes all steps,
     * or when the final step is dismissed.
     */
    fun onComplete()
}

open class DefaultCoachMarkActions : CoachMarkActions {
    override fun onPreviousCalled() {}
    override fun onNextCalled() {}
    override fun onSkipCalled() {}
    override fun onComplete() {}
}