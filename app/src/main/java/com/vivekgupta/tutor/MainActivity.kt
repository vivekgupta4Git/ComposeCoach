package com.vivekgupta.tutor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivekgupta.composecoachmark.coachmark.core.CoachMarkHost
import com.vivekgupta.composecoachmark.coachmark.core.DefaultCoachMarkActions
import com.vivekgupta.composecoachmark.coachmark.core.rememberCoachMarkState
import com.vivekgupta.tutor.ui.CanopasRevealEffect
import com.vivekgupta.tutor.ui.CanopasStyle
import com.vivekgupta.tutor.ui.CircleRevealEffect
import com.vivekgupta.tutor.ui.NoCoachMarkButtons
import com.vivekgupta.tutor.ui.theme.TutorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showCoachMark by remember {
                mutableStateOf(true)
            }
            val coachMarkState = rememberCoachMarkState()
            val scrollState = rememberScrollState()

            TutorTheme {
                CoachMarkHost(
                    showCoach = showCoachMark ,
                    state = coachMarkState,
                    actions = object : DefaultCoachMarkActions(){
                        override fun onComplete() {
                            super.onComplete()
                            Log.d("CoachMark","Show Complete")
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    )
                    {
                        Column(
                            Modifier
                                .safeContentPadding()
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
                                    isOutsideClickDismissable = false,
                                    revealEffect = CanopasRevealEffect(),
                                    backgroundCoachStyle = CanopasStyle(),
                                    content = {
                                        Column(
                                            modifier = Modifier.padding(
                                                horizontal = 20.dp,
                                                vertical = 10.dp
                                            ),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
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
                                        revealEffect = CircleRevealEffect(),
                                        content = {
                                            Column(
                                                modifier = Modifier.padding(horizontal = 20.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {

                                                Text(
                                                    text = "A Highly Customizable Coach Mark Library!!",
                                                    color = Color.White,
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Justify
                                                )

                                            }
                                        },
                                        backgroundCoachStyle = NoCoachMarkButtons
                                    )
                            )
                            Greeting(
                                "Default Style", modifier = Modifier.addTarget(
                                    3,
                                    content = {
                                        Text(
                                            text = "Use Samples to Create your own Style !!",
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                horizontal = 20.dp,
                                                vertical = 10.dp
                                            )
                                        )
                                    },
                                    alignment = Alignment.TopStart,
                                    isForcedAlignment = true
                                )
                            )

                            Greeting("Happy Coding !!", modifier = Modifier)
                            FloatingActionButton(
                                onClick = { }, modifier = Modifier
                                    .addTarget(
                                        4,
                                        revealEffect = CircleRevealEffect(),
                                        content = {
                                            Column(
                                                modifier = Modifier.padding(
                                                    horizontal = 20.dp,
                                                    vertical = 10.dp
                                                ),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
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
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.phone__streamline_plump),
                                    contentDescription = "phone"
                                )
                            }

                            Button(onClick = {
                                state.reset()
                            }
                            ) {
                                Text("Reset")
                            }
                        }
                    }
                }
            }
        }
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

