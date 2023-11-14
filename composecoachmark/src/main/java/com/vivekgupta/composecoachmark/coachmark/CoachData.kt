package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.ui.layout.LayoutCoordinates

/**
 *@author Vivek Gupta on 13-11-23
 */
data class CoachData(
    val coachMarkMessage : String? =null,
    val coachMarkCoordinates: LayoutCoordinates
)
