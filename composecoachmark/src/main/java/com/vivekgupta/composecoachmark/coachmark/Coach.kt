package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 *@author Vivek Gupta on 13-9-23
 */
@Composable
internal fun Coach(
    modifier: Modifier = Modifier,
    coordinates: LayoutCoordinates,
    message : String = "",
    messageBoxShape: Shape = EllipseMessageShape(),
    messageBoxBackgroundColor : Color = Color.White,
    messageBoxTextColor : Color = Color.Black,
    messageBoxTextStyle : TextStyle = TextStyle.Default,
    messageBoxWidth: Dp? = null,
    messageBoxHeight: Dp? = null,
    distanceFromCoordinates : Dp = 50.dp,
    skipButtonModifier: Modifier = Modifier,
    skipButtonText: String = "Skip",
    skipButtonAlignment: Alignment = Alignment.BottomStart,
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor= Color.Black,
    ),
    nextButtonModifier: Modifier = Modifier,
    nextButtonText: String = "Next",
    nextButtonAlignment: Alignment = Alignment.BottomEnd,
    nextButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor= Color.Black,
    ),
    onSkip: () -> Unit = {},
    onNext: () -> Unit,
) {
    val density = LocalDensity.current
    val distance = with(density){
        distanceFromCoordinates.toPx()
    }
    val offsetY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable { onNext() },
        color = Color.Black.copy(alpha = 0.05f)
    ) {
        val bounds = coordinates.boundsInRoot()
        Canvas(modifier = Modifier, onDraw = {

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                //this is must to act as a destination...
                drawRect(Color.Black.copy(alpha = 0.8f))
                //this the source , we are using blend mode to clear the destination pixels
                val x = bounds.topLeft.x - 20f
                val y = bounds.topLeft.y - 20f
                val newBound = Offset(x, y)
                val height = bounds.size.height + 20f
                val width = bounds.size.width + 20f
                val newSize = Size(width,height)
                drawRect(
                    color = Color.White, topLeft = newBound,
                    size = newSize,
                    blendMode = BlendMode.Clear
                )
                /*  drawCircle(bounds.center.x,bounds.center.y,bounds.width/2,android.graphics.Paint().apply {
                      color = android.graphics.Color.WHITE
                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                          blendMode = android.graphics.BlendMode.CLEAR
                      }
                  })*/
                restoreToCount(checkPoint)
            }
        })
        BoxWithConstraints {
            LaunchedEffect(key1 = bounds.bottomRight.y, block = {
                coroutineScope.launch {
                    offsetY.snapTo(0f)
                    offsetY.animateTo(
                        targetValue = bounds.bottomRight.y,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        )
                    )
                }
            })
            CoachMarkMessageBox(
                modifier = Modifier.offset {
                    IntOffset(x = bounds.left.toInt() , y = bounds.bottom.toInt() + distance.toInt())
                },
                backgroundColor = messageBoxBackgroundColor,
                shape = messageBoxShape,
                messageBoxWidth = messageBoxWidth,
                messageBoxHeight = messageBoxHeight
            ) {
                Box(Modifier.fillMaxSize()) {
                    Text(text = message,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                            .align(Alignment.Center),
                        style = messageBoxTextStyle,
                    color = messageBoxTextColor)
                }
            }

            Button(
                onClick = onSkip, modifier = skipButtonModifier
                    .align(skipButtonAlignment),
                colors = skipButtonColors
            ) {
                Text(text = skipButtonText)
            }
            Button(
                onClick = onNext, modifier = nextButtonModifier
                    .align(nextButtonAlignment),
                colors = nextButtonColors
            ) {
                Text(text = nextButtonText)
            }
        }
    }
}

