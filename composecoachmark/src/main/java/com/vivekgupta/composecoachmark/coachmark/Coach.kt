package com.vivekgupta.composecoachmark.coachmark

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layoutId
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
    onBack: () -> Unit = {},
    onSkip: () -> Unit = {},
    onNext: () -> Unit,
) {
    val bounds = coordinates.boundsInRoot()
    val radius = remember {
        Animatable(0f)
    }

    var newOffset = Offset.Zero
    var newSize = Size(0f, 0f)
    val rect = remember {
        Animatable(Rect(offset = bounds.center, size = Size.Zero), rectToVector)
    }
    when (revealAnimation) {
        RevealAnimation.CIRCLE -> {
            LaunchedEffect(key1 = bounds, block = {
                radius.snapTo(0f)
                radius.animateTo(bounds.width / 2 + 10f, tween(500, easing = LinearEasing))
            })
        }

        RevealAnimation.RECTANGLE -> {
            //extra padding from top to reveal view
            val x = bounds.topLeft.x - 20f
            val y = bounds.topLeft.y - 20f
            newOffset = Offset(x, y)
            //extra width and height to reveal view
            val height = bounds.size.height + 40f
            val width = bounds.size.width + 40f
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
    val alphaAnimation = remember { Animatable(0f) }
    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable { onNext() },
        color = Color.Black.copy(alpha = 0.05f)
    ) {
        Canvas(modifier = Modifier, onDraw = {

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                //this is must to act as a destination...
                drawRect(Color.Black.copy(alpha = 0.8f))
                //this the source , we are using blend mode to clear the destination pixels

                when (revealAnimation) {
                    RevealAnimation.RECTANGLE -> {
                        drawRect(
                            color = Color.White, topLeft = rect.value.topLeft,
                            size = rect.value.size,
                            blendMode = BlendMode.Clear
                        )

                    }

                    RevealAnimation.CIRCLE -> {
                        drawCircle(
                            bounds.center.x,
                            bounds.center.y,
                            radius.value,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                            })
                    }
                }


                restoreToCount(checkPoint)
            }
        })
        BoxWithConstraints {
            val currentConstraintScope = this

            val viewPortHeight = with(density) {
                currentConstraintScope.maxHeight.toPx()
            }
            val viewPortWidth = with(density) {
                currentConstraintScope.maxWidth.toPx()
            }

            val newBound = Rect(newOffset, newSize)
            val messageBoxPosition by remember {
                mutableStateOf(MessageBoxPosition(VerticalPosition.BOTTOM,HorizontalPosition.CENTER))
            }

            var xPositionMessageBox by remember {
                mutableStateOf(0.0f)
            }
            var yPositionMessageBox by remember {
                mutableStateOf(0.0f)
            }

            val centerOfViewPort = Offset(viewPortWidth / 2f, viewPortHeight / 2f)

            yPositionMessageBox =
                if (newBound.bottom <= centerOfViewPort.y && newBound.top <= centerOfViewPort.y) {
                    messageBoxPosition.verticalPosition = VerticalPosition.BOTTOM
                    when (revealAnimation) {
                        RevealAnimation.RECTANGLE -> {

                            newBound.bottom + distance
                        }

                        RevealAnimation.CIRCLE -> {
                            newBound.bottom + (newBound.width / 2f) + distance
                        }
                    }
                } else if (newBound.bottom >= centerOfViewPort.y && newBound.top >= centerOfViewPort.y) {
                    messageBoxPosition.verticalPosition = VerticalPosition.TOP

                    when (revealAnimation) {
                        RevealAnimation.RECTANGLE -> {
                            newBound.top - distance
                        }

                        RevealAnimation.CIRCLE -> {
                            newBound.top - (newBound.width / 2f) - distance
                        }
                    }
                } else {
                    messageBoxPosition.verticalPosition = VerticalPosition.CENTER

                    newBound.bottom + distance
                }

            xPositionMessageBox =
                if (newBound.left <= centerOfViewPort.x && newBound.right <= centerOfViewPort.x)
                {
                    messageBoxPosition.horizontalPosition = HorizontalPosition.RIGHT
                    newBound.right + distance
                } else if (newBound.left >= centerOfViewPort.x && newBound.right >= centerOfViewPort.x)
                {
                    messageBoxPosition.horizontalPosition = HorizontalPosition.LEFT

                    when (revealAnimation) {
                        RevealAnimation.RECTANGLE -> {

                            newBound.left - distance
                        }
                        RevealAnimation.CIRCLE -> {
                            newBound.left- (newBound.width / 2f) - distance
                        }
                    }
                } else {
                    messageBoxPosition.horizontalPosition = HorizontalPosition.CENTER

                    newBound.left + distance
                }
            LaunchedEffect(key1 = newBound, block = {
                launch {
                    when(messageBoxPosition.verticalPosition){
                        VerticalPosition.TOP->{
                        //todo adjust height
                        }
                        else ->{

                        }
                    }
                }
                launch {
                    alphaAnimation.snapTo(0f)
                    alphaAnimation.animateTo(1f, tween(1000, easing = LinearEasing))
                }
            })
            CoachMarkMessageBox(
                modifier = Modifier.offset {
                        IntOffset(
                            x = xPositionMessageBox.toInt(),
                            y = yPositionMessageBox.toInt()
                        )
                    }
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

@Composable
fun CoachMarkLayout(
    modifier: Modifier = Modifier,
    mainContent : @Composable () ->Unit,
    dependentContent : @Composable () -> Unit,
) {
    SubcomposeLayout(modifier = modifier){
        constraints ->

        val mainPlaceable = subcompose(SlotsEnum.TARGET,mainContent).map{
            it.measure(constraints)
        }
        val dependentMeasurable = subcompose(SlotsEnum.MESSAGE){
                dependentContent()
        }
        val messageBox = dependentMeasurable.find { it.layoutId == "messageBox" }
        val measuredHeight = messageBox?.measure(constraints)?.height

    layout(0,0){

    }
    }

}


enum class SlotsEnum{
    TARGET,
    MESSAGE
}


enum class VerticalPosition{
    TOP,
    CENTER,
    BOTTOM
}
enum class HorizontalPosition{
    LEFT,
    CENTER,
    RIGHT
}
data class MessageBoxPosition(var verticalPosition: VerticalPosition, var horizontalPosition: HorizontalPosition)