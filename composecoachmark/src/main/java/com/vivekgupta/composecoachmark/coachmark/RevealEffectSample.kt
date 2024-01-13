package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.absoluteValue

class CircleRevealEffect : RevealEffect {
    private val radius: Animatable<Float, AnimationVector1D> = Animatable(0f)
    private val focus: Animatable<Float, AnimationVector1D> = Animatable(1f)
    private val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Restart
    )

    override suspend fun animate(targetBounds: Rect) {
        radius.animateTo(0f)
        radius.animateTo(targetBounds.maxDimension / 2f + 40f, tween(500, easing = LinearEasing))
        focus.snapTo(1f)
        focus.animateTo(targetValue = 0.5f, animationSpec = animationSpec)
    }

    override fun drawTargetShape(targetBounds: Rect, drawScope: DrawScope) {
        drawScope.apply {
            drawCircle(
                color = Color.White,
                radius = targetBounds.maxDimension.absoluteValue * focus.value * 2f,
                center = targetBounds.center,
                alpha = 1 - focus.value,
                blendMode = BlendMode.Overlay
            )
            drawCircle(
                color = Color.White,
                radius = radius.value,
                center = targetBounds.center,
                blendMode = BlendMode.Clear
            )
        }
    }


}

class RectangleRevealEffect : RevealEffect {
    private val rect = Animatable(Rect(offset = Offset.Zero, size = Size.Zero), rectToVector)

    override suspend fun animate(targetBounds: Rect) {
        val x = targetBounds.topLeft.x - 40f
        val y = targetBounds.topLeft.y - 40f
        val newOffset = Offset(x, y)
        val height = targetBounds.size.height + 40f
        val width = targetBounds.size.width + 40f
        val newSize = Size(width, height)
        val newBound = Rect(newOffset, newSize)

        rect.snapTo(Rect(targetBounds.center, size = Size.Zero))
        rect.animateTo(newBound, tween(500, easing = LinearEasing))
    }

    override fun drawTargetShape(targetBounds: Rect, drawScope: DrawScope) {
        drawScope.apply {
            drawRect(
                color = Color.Unspecified,
                size = rect.value.size,
                blendMode = BlendMode.Clear,
                topLeft = rect.value.topLeft
            )
        }
    }
}