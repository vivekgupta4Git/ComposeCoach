package com.vivekgupta.composecoachmark.coachmark

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *@author Vivek Gupta on 13-9-23
 */
 class CoachMarkState(
   coordinates: LayoutCoordinates,
   messageBoxShape : Shape = EllipseMessageShape(),
   messageBoxWidth : Dp = 100.dp,
   messageBoxHeight : Dp = 100.dp,
   @DrawableRes coachMarkImage: Int? = null
){

}