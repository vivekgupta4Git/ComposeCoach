package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 *@author Vivek Gupta on 13-9-23
 */
@Composable
internal fun Coach(
    modifier: Modifier = Modifier,
    coordinates: LayoutCoordinates,
    message: String = "",
    messageBoxShape: Shape = EllipseMessageShape(),
    messageBoxBackgroundColor: Color = Color.White,
    messageBoxTextColor: Color = Color.Black,
    messageBoxTextStyle: TextStyle = TextStyle.Default,
    messageBoxWidth: Dp? = null,
    messageBoxHeight: Dp? = null,
    distanceFromCoordinates: Dp = 50.dp,
    skipButtonModifier: Modifier = Modifier,
    skipButtonText: String = "Skip",
    skipButtonAlignment: Alignment = Alignment.BottomCenter,
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = Color.Black,
    ),
    nextButtonModifier: Modifier = Modifier,
    nextButtonText: String = "Next",
    nextButtonAlignment: Alignment = Alignment.BottomEnd,
    nextButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = Color.Black,
    ),
    backButtonModifier: Modifier = Modifier,
    backButtonText: String = "Back",
    backButtonAlignment: Alignment = Alignment.BottomStart,
    backButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = Color.Black,
    ),
    revealEffect: RevealEffect = RectangleRevealEffect(),
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    onBack: () -> Unit = {},
    onSkip: () -> Unit = {},
    onNext: () -> Unit,
) {
    val bounds = coordinates.boundsInRoot()
    LaunchedEffect(key1 = bounds){
        revealEffect.animate(bounds)
    }

    val density = LocalDensity.current
    val distance = with(density) {
        distanceFromCoordinates.toPx()
    }
    val offsetY = remember { Animatable(0f) }
    val alphaAnimation = remember { Animatable(0f) }
    Surface(modifier = modifier
        .fillMaxSize()
        .graphicsLayer(alpha = 0.99f)
        .pointerInput(bounds) {
            detectTapGestures {
                onNext()
            }
        })
    {
        Canvas(modifier = Modifier, onDraw = {
            drawRect(color = Color.Black.copy(alpha = 0.8f))
            revealEffect.drawTargetShape(bounds,this@Canvas)
        })
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scope = this

            val canvasHeight  = with(density){
                scope.maxHeight.roundToPx()
            }
            val canvasWidth = with(density){
                scope.maxWidth.roundToPx()
            }
            LaunchedEffect(key1 = bounds.bottomRight.y, block = {
                launch {
                    offsetY.snapTo(0f)
                    offsetY.animateTo(
                        targetValue = bounds.bottomRight.y + distance,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        )
                    )
                }
                launch {
                    alphaAnimation.snapTo(0f)
                    alphaAnimation.animateTo(1f, tween(1000, easing = LinearEasing))
                }
            })
            CoachLayout(targetBound = bounds,
                canvasSize = IntSize(canvasWidth, canvasHeight),
                alignment = alignment,
                isForcedAlignment = isForcedAlignment,
            ) {
                CoachMarkMessageBox(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = alphaAnimation.value
                        },
                    backgroundColor = messageBoxBackgroundColor,
                    shape = messageBoxShape,
                    messageBoxWidth = messageBoxWidth,
                    messageBoxHeight = messageBoxHeight
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = message,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .align(Alignment.Center),
                            style = messageBoxTextStyle,
                            color = messageBoxTextColor
                        )
                    }
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
            Button(
                onClick = onBack, modifier = backButtonModifier
                    .align(backButtonAlignment),
                colors = backButtonColors
            ) {
                Text(text = backButtonText)
            }
        }
    }
}

