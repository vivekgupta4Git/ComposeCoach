package com.vivekgupta.composecoachmark.coachmark

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.vivekgupta.composecoachmark.coachmark.core.CoachStyle

object DefaultCoachStyle : CoachStyle {
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
                    containerColor = backGroundColor.invert(),
                    contentColor = backGroundColor
                )
            ) {
                Text(text = "Skip")
            }
            Button(
                onClick = {
                    onNext()
                }, modifier = Modifier.align(Alignment.TopEnd),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backGroundColor.invert(),
                    contentColor = backGroundColor
                )
            ) {
                Text(text = "Next")
            }
            Button(
                onClick = {
                    onBack()
                }, modifier = Modifier.align(Alignment.TopStart),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backGroundColor.invert(),
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