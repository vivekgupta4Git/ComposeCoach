package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
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
import kotlin.math.absoluteValue

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
    revealAnimation: RevealAnimation = RevealAnimation.RECTANGLE,
    alignment: Alignment = Alignment.BottomCenter,
    isForcedAlignment: Boolean = false,
    onBack: () -> Unit = {},
    onSkip: () -> Unit = {},
    onNext: () -> Unit,
) {
    val bounds = coordinates.boundsInRoot()
    val radius = remember {
        Animatable(0f)
    }
    val focus = remember {
            Animatable(1f)
        }
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Restart
    )
    var newOffset = Offset.Zero
    var newSize = Size(0f, 0f)
    val rect = remember {
        Animatable(Rect(offset = bounds.center, size = Size.Zero), rectToVector)
    }
    when (revealAnimation) {
        RevealAnimation.CIRCLE -> {
            LaunchedEffect(key1 = bounds, block = {
                radius.snapTo(0f)
                val maxOf = maxOf(bounds.width, bounds.height)
                radius.animateTo(maxOf / 2 + 20f, tween(500, easing = LinearEasing))
                focus.snapTo(1f)
                focus.animateTo(targetValue = 0.5f,animationSpec=animationSpec)
            })
        }

        RevealAnimation.RECTANGLE -> {
            //extra padding from top to reveal view
            val x = bounds.topLeft.x - 20f
            val y = bounds.topLeft.y - 20f
            newOffset = Offset(x, y)
            //extra width and height to reveal view
            val height = bounds.size.height + 20f
            val width = bounds.size.width + 20f
            newSize = Size(width, height)
            //new bound for the view
            val newBound = Rect(newOffset, newSize)
            //animating view from center
            LaunchedEffect(key1 = newBound, block = {
                rect.snapTo(Rect(bounds.center, size = Size.Zero))
                rect.animateTo(newBound, tween(500, easing = LinearEasing))
            })
        }
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
            when (revealAnimation) {
                RevealAnimation.CIRCLE -> {
                    drawCircle(
                        color = Color.White,
                        radius = maxOf(bounds.width.absoluteValue/2,bounds.height.absoluteValue/2) *  focus.value *3f,
                        center = bounds.center,
                        alpha = 1- focus.value,
                        blendMode = BlendMode.Overlay
                    )
                    drawCircle(
                        color = Color.White,
                        radius = radius.value,
                        center=bounds.center,
                        blendMode = BlendMode.Clear
                    )


                }

                RevealAnimation.RECTANGLE -> {
                    drawRect(
                        color = Color.Unspecified,
                        size = rect.value.size,
                        blendMode = BlendMode.Clear,
                        topLeft = rect.value.topLeft
                    )

                }
            }

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
                revealAnimation = revealAnimation,
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

