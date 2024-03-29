package com.vivekgupta.tutor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivekgupta.composecoachmark.coachmark.CanopasStyle
import com.vivekgupta.composecoachmark.coachmark.CanopasRevealEffect
import com.vivekgupta.composecoachmark.coachmark.CircleRevealEffect
import com.vivekgupta.composecoachmark.coachmark.CoachMark
import com.vivekgupta.composecoachmark.coachmark.DefaultCoachStyle
import com.vivekgupta.composecoachmark.coachmark.addTarget
import com.vivekgupta.composecoachmark.coachmark.addTargetOnPlaced
import com.vivekgupta.composecoachmark.coachmark.invert
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
                            .padding(20.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(120.dp)
                    ) {

                        Greeting(
                            "Canopas FTW",
                            modifier = Modifier.addTarget(
                                1,
                                state = coachMarkState,
                                revealEffect = CanopasRevealEffect(),
                                backgroundCoachStyle = CanopasStyle(),
                                content = {
                                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,) {
                                        Image(
                                            painterResource(id = R.drawable.animated_insightful_bulb),
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp)
                                        )

                                        Text(
                                            text = "Welcome to The Compose Coach",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                        )

                                    }

                                }
                            )
                        )
                        Greeting(
                            "Compose Coach", modifier = Modifier
                                .addTarget(
                                    position = 2,
                                    state = coachMarkState,
                                    revealEffect = CircleRevealEffect(),
                                    content = {
                                            Column(modifier = Modifier.padding(horizontal = 20.dp),
                                            verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,) {

                                                Text(
                                                    text = "A Highly Customizable Coach Mark Library!!",
                                                    color = Color.White,
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Justify
                                                )

                                            }
                                    }
                                )
                        )
                        Greeting(
                            "Default Style", modifier = Modifier.addTargetOnPlaced(
                                3,
                                state = coachMarkState,
                                content = {
                                        Text(text = "Use Samples to Create your own Style !!",
                                            color = Color.White, modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
                                },
                                alignment = Alignment.TopStart,
                                isForcedAlignment = true
                            )
                        )

                        Greeting("Happy Coding !!", modifier = Modifier)
                        FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier
                            .addTarget(4, state = coachMarkState,
                                revealEffect = CircleRevealEffect(),
                                backgroundCoachStyle = DefaultCoachStyle(),
                                content = {
                                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,) {
                                    Image(
                                        painterResource(id = R.drawable.animated_insightful_bulb),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp)
                                    )

                                    Text(
                                        text = "Happy Coding !!",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Justify
                                    )
                                }
                            })

                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "phone")
                        }

                    }

                    /**
                     *
                     * Step 4 : Call CoachMark Composable
                     */
                    CoachMark(
                        coachMarkState = coachMarkState,
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
        text = "$name!",
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

