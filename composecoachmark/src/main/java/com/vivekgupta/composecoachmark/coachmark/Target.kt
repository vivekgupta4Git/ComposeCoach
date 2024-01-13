package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.LayoutCoordinates

/**
 *@author Vivek Gupta on 13-1-24
 */

data class Target(
    val coordinates : LayoutCoordinates,
    val revealEffect: RevealEffect = CircleRevealEffect(),
    val alignment: Alignment = Alignment.BottomCenter,
    val isForcedAlignment : Boolean = false,
    val content : BoxScope.() -> Unit
)