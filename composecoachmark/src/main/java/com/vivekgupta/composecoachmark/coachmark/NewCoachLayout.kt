package com.vivekgupta.composecoachmark.coachmark

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

/**
 *@author Vivek Gupta on 14-1-24
 */
@Composable
fun NewCoachLayout(
    canvasRect: Rect,
    newTargetBound: Rect,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    content: @Composable () -> Unit
) {

    SubcomposeLayout { constraints ->
        val mainPlaceable = subcompose(Unit, content).map {
            it.measure(constraints)
        }
        val contentSize = mainPlaceable.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }
        val xPosition = newTargetBound.center.x.toInt() - contentSize.width/2
        val yPosition = newTargetBound.center.y.toInt()+ newTargetBound.height.toInt()/2
        val contentRect = Rect(offset = newTargetBound.topLeft, size = contentSize.toSize())
        layout(width = canvasRect.width.toInt(), canvasRect.height.toInt()) {
            mainPlaceable.forEach {
                it.placeRelative(xPosition,yPosition)
            }
        }
    }
}

private fun findOffset(
    targetBound: Rect,
    canvasRect: Rect,
    contentSize: IntSize
) : Alignment{

    var validAlignment = validAlignmentList.find { alignment ->
        val topLeftOffset = when (alignment) {
            Alignment.TopStart -> {
                Offset(
                    x = targetBound.topLeft.x - contentSize.width.toFloat(),
                    y = targetBound.topLeft.y - contentSize.height.toFloat()
                )
            }

            Alignment.TopCenter -> {
                Offset(
                    x = targetBound.center.x - contentSize.width / 2f,
                    y = targetBound.center.y - targetBound.height / 2f - contentSize.height.toFloat()
                )
            }

            else -> {
                Offset(0f, 0f)
            }
        }
        val contentRect = Rect(Offset(topLeftOffset.x, topLeftOffset.y), contentSize.toSize())
        val isContainedTopLeft = canvasRect.contains(contentRect.topLeft)
        val isContainedBottomRight = canvasRect.contains(contentRect.bottomRight)
        isContainedBottomRight && isContainedTopLeft
    }
    Log.d("MyLog","validAlignment = $validAlignment ")
    if(validAlignment == null)
            validAlignment  = Alignment.BottomCenter

    return validAlignment
}