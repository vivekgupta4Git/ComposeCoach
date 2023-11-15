package com.vivekgupta.tutor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivekgupta.composecoachmark.coachmark.BubbleMessageBox
import com.vivekgupta.composecoachmark.coachmark.CloudShape
import com.vivekgupta.composecoachmark.coachmark.CoachData
import com.vivekgupta.composecoachmark.coachmark.CoachMark
import com.vivekgupta.composecoachmark.coachmark.EllipseMessageShape
import com.vivekgupta.composecoachmark.coachmark.RevealAnimation
import com.vivekgupta.tutor.ui.theme.TutorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /**
             * Usage of Composable Coach Mark
             */

            /**
             * Step 1 : Create a MutableStateMap
             */
            val coachMarkList = remember {
                mutableStateMapOf<Int, CoachData>()
            }

            /**
             * Step 2 : Create a mutable State Variable to show CoachMark
             */
            var canDrawCoachMark by remember {
                mutableStateOf(true)
            }


            TutorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(Modifier.fillMaxSize()
                    , horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(50.dp)
                    ) {
                        Greeting("Android 1",
                            modifier = Modifier
                                /**
                             * Use onGloballyPositioned or onPlaced modifier on your composable
                             */
                            /**
                             * Use onGloballyPositioned or onPlaced modifier on your composable
                             */
                            .onGloballyPositioned {
                                /**
                                 * update List with co-ordinates and use key for position
                                 *
                                 * Customize Message
                                 */
                                /**
                                 * update List with co-ordinates and use key for position
                                 *
                                 * Customize Message
                                 */
                                coachMarkList[2] = CoachData(
                                    "Android 1", it,
                                    containerShape = EllipseMessageShape(),
                                    containerHeight = 150.dp,
                                    containerWidth = 150.dp,
                                    distanceFromComposable = 50.dp,
                                    revealAnimation = RevealAnimation.RECTANGLE
                                )
                            })
                        Greeting("Android 2 ", modifier = Modifier
                            .onGloballyPositioned {
                                coachMarkList[1] = CoachData(
                                    "Android 2\n yo !! on second line", it,
                                    containerShape = RoundedCornerShape(50),
                                    containerColor = Color.Transparent,
                                    textColor = Color.White,
                                    distanceFromComposable = 50.dp,
                                    textStyle = TextStyle().copy(fontSize = 24.sp),
                                    revealAnimation = RevealAnimation.CIRCLE
                                )
                            })
                        Greeting("Android 3 ", modifier = Modifier
                            .onGloballyPositioned {
                                coachMarkList[3] = CoachData(
                                    "Android 3 Default ", it)
                            })
                    }
                    /**
                     *
                     * Step 4 : Call CoachMark Composable
                     */
                    CoachMark(
                        coachMarkElementList = coachMarkList,
                        showCoachMark = canDrawCoachMark,
                        onCancelled = {
                            canDrawCoachMark = false
                            Log.d(TAG, "On Show Cancelled...")
                        },
                        onCompleted = {
                            canDrawCoachMark = false
                            Log.d(TAG, "End of the show...")
                        },
                        onShowBegin = {
                            Log.d(TAG, "Beginning of the show...")
                        },
                        onBeforeShowingCoachMark = { index, position ->
                            Log.d(TAG, "Before Showing for CoachMark $position, at index = $index")

                        }, onAfterShowingCoachMark = { index, position ->
                            Log.d(TAG, "After Showing for CoachMark $position, at index = $index")
                        })

                }


            }
        }
    }

    companion object {
        const val TAG = "MyCoach"
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TutorTheme {
        Greeting("Android")
    }
}

