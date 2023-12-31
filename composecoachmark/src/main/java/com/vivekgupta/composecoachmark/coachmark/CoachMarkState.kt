package com.vivekgupta.composecoachmark.coachmark

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kotlin.math.roundToInt

/**
 *@author Vivek Gupta on 13-9-23
 */

@Composable
fun CoachLayout(
    modifier: Modifier = Modifier,
    canvasSize: IntSize,
    targetBound: Rect,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    revealAnimation: RevealAnimation,
    content: @Composable () -> Unit
) {

    SubcomposeLayout(modifier = modifier) { constraints ->
        val mainPlaceable = subcompose(Unit, content).map {
            it.measure(constraints)
        }
        val messageBoxSize = mainPlaceable.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }

        val (xPosition, yPosition) = if (!isForcedAlignment)
                                            findVisiblePositions(
                                                revealAnimation,
                                                targetBound,
                                                messageBoxSize,
                                                canvasSize)
                                        else
                                            returnPositionBasedOnAlignment(
                                                alignment,
                                                targetBound,
                                                messageBoxSize,
                                                revealAnimation
                                            )

        layout(width = canvasSize.width, canvasSize.height) {
            mainPlaceable.forEach {
                it.placeRelative(xPosition.roundToInt(), yPosition.roundToInt())
            }
        }
    }
}

private val validAlignmentList = listOf(
    Alignment.BottomStart,
    Alignment.BottomCenter,
    Alignment.BottomEnd,
    Alignment.CenterStart,
    Alignment.Center,
    Alignment.CenterEnd,
    Alignment.TopStart,
    Alignment.TopCenter,
    Alignment.TopEnd,
)

private fun findVisiblePositions(
    revealAnimation: RevealAnimation,
    targetBound: Rect,
    messageBoxSize: IntSize,
    canvasSize: IntSize
): Pair<Float, Float> {

    val adjustedY = when (revealAnimation) {
        RevealAnimation.RECTANGLE -> {
            targetBound.height.toInt()
        }

        RevealAnimation.CIRCLE -> {
            val maxOf = maxOf(targetBound.width, targetBound.height)
            (maxOf / 2).toInt()
        }
    }
    var validAlignment = validAlignmentList.find { alignment ->
        val (xPositionOfMsg, yPositionOfMsg, adjustOffset) = when (alignment) {
            Alignment.TopStart -> {
                Triple(
                    targetBound.topLeft.x,
                    targetBound.topLeft.y,
                    Offset(messageBoxSize.width.toFloat(), messageBoxSize.height.toFloat())
                )
            }

            Alignment.TopCenter -> {
                Triple(
                    targetBound.topCenter.x,
                    targetBound.topCenter.y,
                    Offset(
                        messageBoxSize.width.toFloat() / 2,
                        messageBoxSize.height.toFloat() + adjustedY
                    )
                )
            }

            Alignment.TopEnd -> {
                Triple(
                    targetBound.topRight.x,
                    targetBound.topRight.y,
                    Offset(0f, messageBoxSize.height.toFloat())
                )
            }

            Alignment.BottomStart -> {
                Triple(
                    targetBound.bottomLeft.x,
                    targetBound.bottomLeft.y,
                    Offset(messageBoxSize.width.toFloat(), 0f)
                )
            }

            Alignment.BottomCenter, Alignment.Center -> {
                Triple(
                    targetBound.bottomCenter.x,
                    targetBound.bottomCenter.y,
                    Offset(targetBound.width / 2f, -adjustedY.toFloat())
                )
            }

            Alignment.BottomEnd -> {
                Triple(targetBound.bottomRight.x, targetBound.bottomRight.y, Offset(0f, 0f))
            }

            Alignment.CenterStart -> {
                Triple(
                    targetBound.centerLeft.x,
                    targetBound.centerLeft.y,
                    Offset(messageBoxSize.width.toFloat(), messageBoxSize.height.toFloat() / 2)
                )
            }

            Alignment.CenterEnd -> {
                Triple(
                    targetBound.centerRight.x,
                    targetBound.centerRight.y,
                    Offset(0f, messageBoxSize.height.toFloat() / 2)
                )
            }

            else -> {
                Triple(0f, 0f, Offset(0f, 0f))
            }
        }

        val canvasRect = Rect(Offset.Zero, canvasSize.toSize())

        val xPosition = xPositionOfMsg - adjustOffset.x
        val yPosition = yPositionOfMsg - adjustOffset.y

        val messageBoxRect = Rect(Offset(xPosition, yPosition), messageBoxSize.toSize())
        val isContainedTopLeft = canvasRect.contains(messageBoxRect.topLeft)
        val isContainedBottomRight = canvasRect.contains(messageBoxRect.bottomRight)
        Log.d("MyLog", "For Alignment = $alignment isContained = $isContainedTopLeft and $isContainedBottomRight")
        isContainedTopLeft && isContainedBottomRight
    }

    if(validAlignment==null)
    {
        Log.d("MyLog","Valid Alignment is Null")
        validAlignment =Alignment.BottomCenter
    }
    return returnPositionBasedOnAlignment(
        validAlignment,
        targetBound,
        messageBoxSize,
        revealAnimation
    )
}

private fun returnPositionBasedOnAlignment(
    alignment: Alignment,
    targetBound: Rect,
    messageBoxSize: IntSize,
    revealAnimation: RevealAnimation
): Pair<Float, Float> {
    val adjustedY = when (revealAnimation) {
        RevealAnimation.RECTANGLE -> {
            targetBound.height.toInt()
        }

        RevealAnimation.CIRCLE -> {
            val maxOf = maxOf(targetBound.width, targetBound.height)
            (maxOf / 2).toInt()
        }
    }
    val (xPositionOfMsg, yPositionOfMsg, adjustOffset) = when (alignment) {
        Alignment.TopStart -> {
            Triple(
                targetBound.topLeft.x,
                targetBound.topLeft.y,
                Offset(messageBoxSize.width.toFloat(), messageBoxSize.height.toFloat())
            )
        }

        Alignment.TopCenter -> {
            Triple(
                targetBound.topCenter.x,
                targetBound.topCenter.y,
                Offset(
                    messageBoxSize.width.toFloat() / 2,
                    messageBoxSize.height.toFloat() + adjustedY
                )
            )
        }

        Alignment.TopEnd -> {
            Triple(
                targetBound.topRight.x,
                targetBound.topRight.y,
                Offset(0f, messageBoxSize.height.toFloat())
            )
        }

        Alignment.BottomStart -> {
            Triple(
                targetBound.bottomLeft.x,
                targetBound.bottomLeft.y,
                Offset(messageBoxSize.width.toFloat(), 0f)
            )
        }

        Alignment.BottomCenter, Alignment.Center -> {
            Triple(
                targetBound.bottomCenter.x,
                targetBound.bottomCenter.y,
                Offset(targetBound.width / 2f, -adjustedY.toFloat())
            )
        }

        Alignment.BottomEnd -> {
            Triple(targetBound.bottomRight.x, targetBound.bottomRight.y, Offset(0f, 0f))
        }

        Alignment.CenterStart -> {
            Triple(
                targetBound.centerLeft.x,
                targetBound.centerLeft.y,
                Offset(messageBoxSize.width.toFloat(), messageBoxSize.height.toFloat() / 2)
            )
        }

        Alignment.CenterEnd -> {
            Triple(
                targetBound.centerRight.x,
                targetBound.centerRight.y,
                Offset(0f, messageBoxSize.height.toFloat() / 2)
            )
        }

        else -> {
            Triple(0f, 0f, Offset(0f, 0f))
        }
    }

    val xPosition = xPositionOfMsg - adjustOffset.x
    val yPosition = yPositionOfMsg - adjustOffset.y
    return Pair(xPosition, yPosition)
}
