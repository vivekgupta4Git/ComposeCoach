package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.vivekgupta.composecoachmark.coachmark.core.RevealEffect

object DefaultRevealEffect : RevealEffect {
    private val rect = Animatable(Rect(offset = Offset.Zero, size = Size.Zero), rectToVector)

    override suspend fun enterAnimation(targetBounds: Rect) {
        val x = targetBounds.topLeft.x - 50f
        val y = targetBounds.topLeft.y - 50f
        val newOffset = Offset(x, y)
        val height = targetBounds.size.height + 100f
        val width = targetBounds.size.width + 100f
        val newSize = Size(width, height)
        val newBound = Rect(newOffset, newSize)

        rect.snapTo(Rect(targetBounds.center, size = Size.Zero))
        rect.animateTo(newBound, tween(500, easing = LinearEasing))

    }

    override suspend fun exitAnimation(targetBounds: Rect) {
        rect.animateTo(
            Rect(targetBounds.center, size = Size.Zero),
            tween(500, easing = LinearEasing)
        )
    }

    override fun drawTargetShape(targetBounds: Rect, drawScope: DrawScope): Rect {
        val x = targetBounds.topLeft.x - 50f
        val y = targetBounds.topLeft.y - 50f
        val newOffset = Offset(x, y)
        val height = targetBounds.size.height + 100f
        val width = targetBounds.size.width + 100f
        val newSize = Size(width, height)
        val newBound = Rect(newOffset, newSize)
        drawScope.apply {
            drawRect(
                color = Color.White,
                size = rect.value.size,
                blendMode = BlendMode.Clear,
                topLeft = rect.value.topLeft
            )
        }
        return newBound
    }
}