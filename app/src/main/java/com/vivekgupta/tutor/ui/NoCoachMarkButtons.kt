package com.vivekgupta.tutor.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.vivekgupta.composecoachmark.coachmark.core.CoachStyle

object NoCoachMarkButtons : CoachStyle {
    override val backGroundColor: Color
        get() = Color.Companion.Blue
    override val backgroundAlpha: Float
        get() = 0.8f

    @Composable
    override fun drawCoachButtons(
        contentScope: BoxWithConstraintsScope,
        targetBounds: Rect,
        onBack: () -> Unit,
        onSkip: () -> Unit,
        onNext: () -> Unit
    ) {

    }

    override fun drawCoachShape(
        targetBounds: Rect,
        drawScope: DrawScope
    ): Rect {
        drawScope.apply {
            drawRect(color = backGroundColor.copy(alpha = backgroundAlpha))
        }
        return Rect(Offset.Companion.Zero, size = drawScope.size)
    }
}