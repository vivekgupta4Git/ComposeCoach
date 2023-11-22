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
    skipButtonModifier: Modifier = Modifier,
    skipButtonText: String = "Skip",
    skipButtonAlignment: Alignment = Alignment.BottomCenter,
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
    backButtonModifier: Modifier = Modifier,
    backButtonText: String = "Back",
    backButtonAlignment: Alignment = Alignment.BottomStart,
    backButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor= Color.Black,
    ),
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
            val nextValue =  coachMarkElementList.nextValue(count)
            Coach(
                coordinates = nextValue.coordinates,
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
                }
            , message = nextValue.message ,
                messageBoxShape = nextValue.containerShape,
                messageBoxTextStyle = nextValue.textStyle,
                messageBoxBackgroundColor = nextValue.containerColor,
                messageBoxTextColor = nextValue.textColor,
                distanceFromCoordinates = nextValue.distanceFromComposable,
                messageBoxHeight = nextValue.containerHeight,
                messageBoxWidth = nextValue.containerWidth,
                revealAnimation = nextValue.revealAnimation,
                isForcedAlignment = nextValue.isForcedAlignment,
                alignment = nextValue.alignment,
                skipButtonAlignment = skipButtonAlignment,
                skipButtonColors = skipButtonColors,
                skipButtonModifier = skipButtonModifier,
                skipButtonText = skipButtonText,
                nextButtonAlignment = nextButtonAlignment,
                nextButtonColors = nextButtonColors,
                nextButtonText = nextButtonText,
                nextButtonModifier = nextButtonModifier,
                backButtonAlignment = backButtonAlignment,
                backButtonColors = backButtonColors,
                backButtonText = backButtonText,
                backButtonModifier = backButtonModifier,
                modifier = modifier,
            )
        }
    }
}