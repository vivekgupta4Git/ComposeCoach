package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
fun Modifier.addTarget(
    position : Int,
    state : CoachMarkState,
    message : String,
    containerShape : Shape = CloudShape,
    containerHeight : Dp? = null,
    containerWidth : Dp? = null,
    containerColor : Color = Color.White,
    textStyle : TextStyle = TextStyle.Default,
    textColor : Color = Color.Black,
    canPointToComposable : Boolean = true,      //this will draw an arrow to the co-ordinates.
    distanceFromComposable : Dp = 10.dp,
    revealEffect: RevealEffect = RectangleRevealEffect(),
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment : Boolean = false,
    ) = onGloballyPositioned {
    layoutCoordinates ->
    state.targetList[position] = CoachData(
        message = message,
        coordinates = layoutCoordinates,
        containerShape,
        containerHeight,
        containerWidth,
        containerColor,
        textStyle,
        textColor,
        canPointToComposable,
        distanceFromComposable,
        revealEffect,
        alignment,
        isForcedAlignment,
    )
}

class CoachMarkState{
    val targetList = mutableStateMapOf<Int, CoachData>()
}

@Composable
fun rememberCoachMarkState() : CoachMarkState {
    return remember {
        CoachMarkState()
    }
}
