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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    messageBoxShape: Shape = CloudShape,
    messageBoxWidth : Dp = 120.dp,
    messageBoxHeight : Dp = 100.dp,
    messageBoxModifier : Modifier = Modifier,
    messageBoxTextStyle : TextStyle = TextStyle.Default,
    skipButtonModifier: Modifier = Modifier,
    skipButtonText: String = "Skip",
    skipButtonAlignment: Alignment = Alignment.BottomStart,
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor= Color.Black,
    ),
    nextButtonModifier: Modifier = Modifier,
    nextButtonText: String = "Next",
    nextButtonAlignment: Alignment = Alignment.BottomEnd,
    nextButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor= Color.Black,
    ),
    onCancelled: () -> Unit,
    onCompleted: () -> Unit
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
            Coach(
                coordinates = coachMarkElementList.nextValue(count).coachMarkCoordinates,
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
                }
            , message = coachMarkElementList.nextValue(count).coachMarkMessage ?: "",
                messageBoxModifier = messageBoxModifier,
                messageBoxHeight = messageBoxHeight,
                messageBoxWidth = messageBoxWidth,
                messageBoxShape = messageBoxShape,
                messageBoxTextStyle = messageBoxTextStyle,
                skipButtonAlignment = skipButtonAlignment,
                skipButtonColors = skipButtonColors,
                skipButtonModifier = skipButtonModifier,
                skipButtonText = skipButtonText,
                nextButtonAlignment = nextButtonAlignment,
                nextButtonColors = nextButtonColors,
                nextButtonText = nextButtonText,
                nextButtonModifier = nextButtonModifier,
                modifier = modifier,
            )
        }
    }
}