package com.vivekgupta.composecoachmark.coachmark

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
    val message : String,
    val coordinates: LayoutCoordinates,
    val containerShape : Shape = CloudShape,
    val containerHeight : Dp? = null,
    val containerWidth : Dp? = null,
    val containerColor : Color = Color.White,
    val textStyle : TextStyle = TextStyle.Default,
    val textColor : Color = Color.Black,
    val revealEffect: RevealEffect = RectangleRevealEffect(),
    val alignment: Alignment = Alignment.BottomCenter,
    val isForcedAlignment : Boolean = false,
    val coachStyle: CoachStyle = DefaultCoachStyle()
)


