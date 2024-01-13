package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *@author Vivek Gupta on 13-11-23
 */

data class CoachData(
    val coordinates: LayoutCoordinates,
    val content : @Composable (BoxWithConstraintsScope.() -> Unit),
    val revealEffect: RevealEffect = RectangleRevealEffect(),
    val alignment: Alignment = Alignment.BottomCenter,
    val isForcedAlignment : Boolean = false,
    val coachStyle: CoachStyle = DefaultCoachStyle()
)


