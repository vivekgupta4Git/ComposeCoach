package com.vivekgupta.composecoachmark.coachmark

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.absoluteValue

class DefaultCoachStyle : CoachStyle {
    override val backGroundColor: Color
        get() = Color.Black
    override val backgroundAlpha: Float
        get() = 0.8f


    @SuppressLint("ComposableNaming")
    @Composable
    override fun drawCoachButtons(
        contentScope: BoxWithConstraintsScope,
        targetBounds: Rect,
        onBack: () -> Unit,
        onSkip: () -> Unit,
        onNext: () -> Unit
    ) {
        contentScope.apply {
            Button(
                onClick = {
                    onSkip()
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backGroundColor.invert(),
                    contentColor = backGroundColor
                )
            ) {
                Text(text = "Skip")
            }
            Button(
                onClick = {
                    onNext()
                }, modifier = Modifier.align(Alignment.CenterEnd),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backGroundColor.invert(),
                    contentColor = backGroundColor
                )
            ) {
                Text(text = "Next")
            }
            Button(
                onClick = {
                    onBack()
                }, modifier = Modifier.align(Alignment.CenterStart),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backGroundColor.invert(),
                    contentColor = backGroundColor
                )
            ) {
                Text(text = "Back")
            }
        }
    }

    override fun drawCoachShape(targetBounds: Rect, drawScope: DrawScope): Rect {
        drawScope.apply {
            drawRect(color = backGroundColor.copy(alpha = backgroundAlpha))
        }
        return Rect(Offset.Zero, size = drawScope.size)
    }

}

class CanopasStyle : CoachStyle {
    override val backGroundColor: Color
        get() = Color.Blue
    override val backgroundAlpha: Float
        get() = 0.8f
    private val radius :Animatable<Float,AnimationVector1D> = Animatable(0f)

    @SuppressLint("ComposableNaming")
    @Composable
    override fun drawCoachButtons(
        contentScope: BoxWithConstraintsScope,
        targetBounds: Rect,
        onBack: () -> Unit,
        onSkip: () -> Unit,
        onNext: () -> Unit
    ) {
        LaunchedEffect(key1 = targetBounds, block = {
            radius.animateTo(targetBounds.maxDimension.absoluteValue*3f,
            animationSpec = tween(2000, easing = FastOutSlowInEasing)
            )
        })
        contentScope.apply {
            Button(onClick = {
                onSkip()
            }, modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = "Skip")
            }
            Button(onClick = {
                onNext()
            }, modifier = Modifier.align(Alignment.BottomEnd)) {
                Text(text = "Next")
            }
        }
    }

    override fun drawCoachShape(targetBounds: Rect, drawScope: DrawScope): Rect {
        drawScope.apply {
            drawCircle(
                color = backGroundColor,
                radius = radius.value,
                center = targetBounds.center,
                alpha = backgroundAlpha
            )
        }
        return Rect(
            offset = Offset.Zero -targetBounds.topLeft, size =
            Size(
                width = drawScope.size.width,
                height = targetBounds.maxDimension.absoluteValue * 3f
            )
        )
    }

}