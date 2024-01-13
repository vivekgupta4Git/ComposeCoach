package com.vivekgupta.composecoachmark.coachmark

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 *@author Vivek Gupta on 13-1-24
 */
interface  CoachStyle {
    val backGroundColor: Color
    val backgroundAlpha: Float

    /**
     * @Composable
     * Draws the buttons for navigation between targets.
     * Important: Implementations of this method in subclasses must also be annotated with @Composable.
     * @param contentScope The [BoxWithConstraintsScope] for placing the buttons within the coach's content area.
     * @param targetBounds The [Rect] representing the target element that the coach is highlighting.
     * @param onBack A lambda to be invoked when the back button is pressed.
     * @param onSkip A lambda to be invoked when the skip button is pressed.
     * @param onNext A lambda to be invoked when the next button is pressed.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun drawCoachButtons(
        contentScope : BoxWithConstraintsScope,
        targetBounds: Rect,
        onBack: () -> Unit,
        onSkip: () -> Unit,
        onNext: () -> Unit
    )
    /**
     *Draws the shape around the Target
     *@param targetBounds The [Rect] defining the bounds of the target element to be highlighted.
     *@param drawScope The [DrawScope] providing functions for drawing shapes, paths, text, and other visual elements.
     */
    fun drawCoachShape(targetBounds: Rect,
                       drawScope: DrawScope)
}

