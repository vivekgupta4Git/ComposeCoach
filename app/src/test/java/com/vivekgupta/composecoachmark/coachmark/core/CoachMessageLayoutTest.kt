package com.vivekgupta.composecoachmark.coachmark.core

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.ruviapps.coachmark.core.calculateAdaptiveOffset
import com.ruviapps.coachmark.core.findOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class CoachMessageLayoutLogicTest {

    private val canvas = Rect(0f, 0f, 1080f, 2400f)

    @Test
    fun `when enough space below, adaptive offset should place content below target`() {
        val target = Rect(100f, 1000f, 300f, 1200f)
        val contentSize = IntSize(200, 200)

        val offset = calculateAdaptiveOffset(canvas, target, contentSize)

        // Should be placed just below target
        assertEquals(target.bottom, offset.y, 0.1f)
    }

    @Test
    fun `when more space above, adaptive offset should place content above target`() {
        val target = Rect(100f, 1800f, 300f, 2000f)
        val contentSize = IntSize(200, 200)

        val offset = calculateAdaptiveOffset(canvas, target, contentSize)

        // Should be placed just above target
        assertEquals(target.top - contentSize.height, offset.y, 0.1f)
    }

    @Test
    fun `adaptive offset should clamp X position within canvas`() {
        val target = Rect(1000f, 500f, 1100f, 600f)
        val contentSize = IntSize(300, 100)

        val offset = calculateAdaptiveOffset(canvas, target, contentSize)

        // Should not go off screen
        assert(offset.x in 0f..(canvas.width - contentSize.width))
    }

    @Test
    fun `findOffset with BottomCenter aligns below target center`() {
        val target = Rect(100f, 100f, 300f, 200f)
        val contentSize = IntSize(100, 50)

        val offset = findOffset(canvas, target, contentSize, Alignment.BottomCenter)

        val expectedX = target.center.x - contentSize.width / 2f
        val expectedY = target.bottom
        assertEquals(expectedX, offset.x, 0.1f)
        assertEquals(expectedY, offset.y, 0.1f)
    }

    @Test
    fun `findOffset clamps to canvas boundaries`() {
        val smallCanvas = Rect(0f, 0f, 100f, 100f)
        val target = Rect(90f, 90f, 110f, 110f)
        val contentSize = IntSize(200, 200)

        val offset = findOffset(smallCanvas, target, contentSize, Alignment.BottomEnd)

        // Expect clamped to top-left of canvas (0,0)
        assertEquals(0f, offset.x, 0.1f)
        assertEquals(0f, offset.y, 0.1f)
    }
}
