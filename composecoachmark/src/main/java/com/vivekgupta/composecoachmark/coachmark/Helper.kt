package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

/**
 *@author Vivek Gupta on 13-11-23
 */


internal fun Map<Int, CoachData>.hasNextValue(dropCount : Int)= values.drop(dropCount.coerceAtLeast(0)).iterator().hasNext()
internal fun Map<Int, CoachData>.nextKey(dropCount: Int) = keys.drop(dropCount.coerceAtLeast(0)).iterator().next()
internal fun Map<Int, CoachData>.firstKey(dropCount : Int) = keys.drop(dropCount.coerceAtLeast(0)).first()
internal fun Map<Int, CoachData>.nextValue(dropCount: Int) = values.drop(dropCount.coerceAtLeast(0)).iterator().next()
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