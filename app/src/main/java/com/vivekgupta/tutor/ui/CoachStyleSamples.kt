package com.vivekgupta.tutor.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.vivekgupta.composecoachmark.coachmark.core.CoachStyle
import com.vivekgupta.composecoachmark.coachmark.invert
import kotlin.math.absoluteValue
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
            offset = Offset.Zero, size =
            Size(
                width = drawScope.size.width,
                height = targetBounds.maxDimension.absoluteValue * 3f
            )
        )
    }

}