package com.vivekgupta.composecoachmark.coachmark

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 *@author Vivek Gupta on 13-11-23
 */
@Composable
fun CoachMark(
    modifier: Modifier = Modifier,
    coachMarkElementList: Map<Int, CoachData>,
    showCoachMark: Boolean = true,
    onShowBegin: () -> Unit = {},
    onBeforeShowingCoachMark: (Int, Int) -> Unit = { _, _ -> },
    onAfterShowingCoachMark : (Int, Int) -> Unit = { _ , _-> },
    onBack : () -> Unit = {},
    onCancelled: () -> Unit,
    onCompleted: () -> Unit,
) {
    var canDrawCoachMark by rememberSaveable {
        mutableStateOf(showCoachMark)
    }
    var count by rememberSaveable {
        mutableStateOf(0)
    }
    var canShowNext by rememberSaveable(coachMarkElementList.size) {
        mutableStateOf(coachMarkElementList.hasNextValue(count) && canDrawCoachMark)
    }

    val onShowStart by rememberUpdatedState(onShowBegin)

    if (canShowNext) {
        if (coachMarkElementList.isNotEmpty()) {
            //even after the recomposition this shouldn't fire again
            LaunchedEffect(key1 = Unit) {
                onShowStart()
            }
            LaunchedEffect(key1 = coachMarkElementList.nextKey(count), block = {
                onBeforeShowingCoachMark(count,coachMarkElementList.firstKey(count))
            })
            val currentTarget =  coachMarkElementList.nextValue(count)
            Coach(
                content = currentTarget.content,
                coordinates = currentTarget.coordinates,
                onBack = {
                    canShowNext = if(count == 0) false
                    else {
                        onBack()
                        --count
                        coachMarkElementList.hasNextValue(count)

                    }
                },
                onSkip = {
                    canDrawCoachMark = false
                    canShowNext = false
                    onCancelled()
                },
                onNext = {
                    onAfterShowingCoachMark(count,coachMarkElementList.firstKey(count))
                    ++count
                    canShowNext = coachMarkElementList.hasNextValue(count)
                    //reset count and mark finish
                    if (!canShowNext) {
                        count = 0
                        onCompleted()
                        canDrawCoachMark = false
                    }
                },
                isForcedAlignment = currentTarget.isForcedAlignment,
                alignment = currentTarget.alignment,
                modifier = modifier,
                revealEffect = currentTarget.revealEffect,
                coachStyle = currentTarget.coachStyle
            )
        }
    }
}