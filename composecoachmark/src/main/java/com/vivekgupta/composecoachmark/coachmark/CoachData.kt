package com.vivekgupta.composecoachmark.coachmark

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
    val canPointToComposable : Boolean = true,      //this will draw an arrow to the co-ordinates.
    val distanceFromComposable : Dp = 10.dp,
    val revealAnimation : RevealAnimation = RevealAnimation.RECTANGLE
)

enum class RevealAnimation{
    CIRCLE,
    RECTANGLE
}