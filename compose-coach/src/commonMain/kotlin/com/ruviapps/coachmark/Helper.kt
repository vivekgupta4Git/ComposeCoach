package com.ruviapps.coachmark

import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 *@author Vivek Gupta on 13-11-23
 */

/**
 *a position based object should be converted to
 * AnimationVector2D, whereas an object that describes
 * rectangle bounds should convert to AnimationVector4D.
 */
internal val rectToVector = TwoWayConverter(
    convertToVector = { rect: Rect ->
        AnimationVector4D(rect.left, rect.top, rect.width, rect.height)
    },
    convertFromVector = { vector: AnimationVector4D ->
        Rect(
            offset = Offset(vector.v1, vector.v2),
            size = Size(vector.v3, vector.v4)
        )
    }
)

fun Color.invert(): Color {
    val red = 1f - this.red
    val green = 1f - this.green
    val blue = 1f - this.blue
    return Color(red, green, blue,this.alpha,this.colorSpace)
}
