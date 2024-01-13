package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 *@author Vivek Gupta on 13-1-24
 */

interface RevealEffect {
    /**
     * method to animate shape defined by [drawTargetShape] method around target's bound
     */
    suspend fun animate(targetBounds: Rect)

    /**
     * method to draw Target Shape by using drawScope
     */
    fun drawTargetShape(targetBounds: Rect, drawScope: DrawScope)
}


