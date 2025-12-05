package com.ruviapps.shared.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ruviapps.coachmark.core.CoachMessageLayout
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoachMessageLayoutUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun coachMessage_isAdaptivelyPlacedAboveOrBelowTarget() {
        val canvas = Rect(0f, 0f, 1080f, 1200f) // Simulate smaller device
        val target = Rect(100f, 800f, 300f, 1000f)

        composeRule.setContent {
            CoachMessageLayout(
                canvasRect = canvas,
                targetBound = target
            ) {
                Box(Modifier.testTag("message").size(200.dp))
            }
        }

        val position = composeRule.onNodeWithTag("message").fetchSemanticsNode().positionInRoot
        val contentHeight = 200f * composeRule.density.density

        val availableAbove = target.top
        val availableBelow = canvas.height - target.bottom

        if (availableBelow >= availableAbove) {
            // Expect below
            assert(position.y >= target.bottom - 1)
        } else {
            // Expect above
            assert(position.y + contentHeight <= target.top + 1)
        }
    }


    @Test
    fun coachMessage_respectsForcedAlignment() {
        val canvas = Rect(0f, 0f, 1080f, 2400f)
        val target = Rect(100f, 100f, 300f, 300f)

        composeRule.setContent {
            CoachMessageLayout(
                canvasRect = canvas,
                targetBound = target,
                alignment = Alignment.TopCenter,
                isForcedAlignment = true
            ) {
                Box(Modifier.testTag("forced").size(100.dp))
            }
        }

        val position = composeRule.onNodeWithTag("forced").fetchSemanticsNode().positionInRoot

        // Expect Y to be above target
        assert(position.y <= target.top)
    }
}