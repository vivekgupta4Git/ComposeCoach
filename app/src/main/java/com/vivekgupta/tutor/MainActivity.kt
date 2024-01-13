package com.vivekgupta.tutor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivekgupta.composecoachmark.coachmark.CanopasStyle
import com.vivekgupta.composecoachmark.coachmark.CircleRevealEffect
import com.vivekgupta.composecoachmark.coachmark.CoachMark
import com.vivekgupta.composecoachmark.coachmark.addTarget
import com.vivekgupta.composecoachmark.coachmark.rememberCoachMarkState
import com.vivekgupta.tutor.ui.theme.TutorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /**
             * Usage of Composable Coach Mark
             */

            /**
             * Step 1 : Create a CoachMarkState
             */
            val coachMarkState = rememberCoachMarkState()

            /**
             * Step 2 : Create a mutable State Variable to show CoachMark
             */
            var canDrawCoachMark by remember {
                mutableStateOf(true)
            }
            val scrollState = rememberScrollState()
            //   val scope = rememberCoroutineScope()
            TutorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(100.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(200.dp)
                    ) {
                        Greeting(
                            "Android 1",
                            modifier = Modifier.addTarget(
                                1,
                                message = "This is first ",
                                containerShape = CutCornerShape(10.dp),
                                containerHeight = 150.dp,
                                containerWidth = 150.dp,
                                state = coachMarkState,
                                style = CanopasStyle()
                            )
                        )
                        Greeting(
                            "Android 2 ", modifier = Modifier
                                .addTarget(
                                    position = 2,
                                    message = "Android 2\n yo !! on second line",
                                    containerShape = RoundedCornerShape(50),
                                    containerColor = Color.Transparent,
                                    textColor = Color.White,
                                    isForcedAlignment = false,
                                    textStyle = TextStyle().copy(fontSize = 24.sp),
                                    state = coachMarkState,
                                    alignment = Alignment.TopCenter,
                                    revealEffect = CircleRevealEffect()
                                )
                        )
                        Greeting(
                            "Android 3 ", modifier = Modifier.addTarget(
                                3,
                                state = coachMarkState, message = "abc",
                                containerShape = RoundedCornerShape(20),
                                containerHeight = 100.dp,
                                containerWidth = 200.dp,
                            )
                        )

                        Greeting("Android 4 ", modifier = Modifier)
                    }
                    /**
                     *
                     * Step 4 : Call CoachMark Composable
                     */
                    CoachMark(
                        coachMarkElementList = coachMarkState.targetList,
                        showCoachMark = canDrawCoachMark,
                        onBack = {
                            Log.d(TAG, "Re-showing CoachMark!!")
                        },
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

                        }
                    )

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

