package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *@author Vivek Gupta on 13-9-23
 */
@Composable
internal fun CoachMarkMessageBox(
    modifier: Modifier = Modifier,
    shape: Shape = EllipseMessageShape(),
    width: Dp = 100.dp,
    height: Dp = 100.dp,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    content: @Composable () -> Unit
) {
    Surface(shape = shape, modifier = modifier.requiredSize(width = width, height = height), color = backgroundColor, contentColor = contentColor) {
        content()
    }

}