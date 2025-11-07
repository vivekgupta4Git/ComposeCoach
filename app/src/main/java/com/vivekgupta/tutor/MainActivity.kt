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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.vivekgupta.composecoachmark.coachmark.core.CoachMarkScope
import com.vivekgupta.composecoachmark.coachmark.core.EmptyCoachMarkEventListener
import com.vivekgupta.composecoachmark.coachmark.core.rememberCoachMarkState
import com.vivekgupta.tutor.ui.CanopasRevealEffect
import com.vivekgupta.tutor.ui.CanopasStyle
import com.vivekgupta.tutor.ui.CircleRevealEffect
import com.vivekgupta.tutor.ui.NoCoachMarkButtons
import com.vivekgupta.tutor.ui.theme.TutorTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showCoachMark by rememberSaveable {
                mutableStateOf(true)
            }
            val coachMarkState = rememberCoachMarkState(
                listener = object : EmptyCoachMarkEventListener() {
                    override fun onComplete() {
                        showCoachMark = false
                        Log.d("CoachMark", "Show Complete")
                    }

                    override fun onNextCalled() {
                        Log.d("CoachMark","On Next Called...")
                    }

                    override fun onSkipCalled() {
                        Log.d("CoachMark","On Skip Called...")
                    }
                }
            )
            val scrollState = rememberScrollState()

            TutorTheme {
                CoachMarkHost(
                    showCoach = showCoachMark,
                    state = coachMarkState,
                ) {
                    Scaffold(
                        topBar = {
                            CoachMarkTopBar()
                        },
                        floatingActionButton = {
                            CoachMarkFloatingActionButton()
                        }
                    ) {
                        Column(
                            Modifier
                                .padding(it)
                                .fillMaxSize()
                                .padding(20.dp)
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(120.dp)
                        ) {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(3) { index ->
                                    Text(
                                        " Hello $index",
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .addTarget(position = index + 1) {
                                                Text("Hello $index")
                                            }
                                    )
                                }
                            }
                            Greeting(
                                "Canopus FTW",
                                modifier = Modifier.addTarget(
                                    6,
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
                                                color = Color.Black,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                )
                            )
                            Button(
                                onClick = {
                                    showCoachMark = true
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
private fun CoachMarkScope.CoachMarkFloatingActionButton() {
    FloatingActionButton(
        onClick = { },
        modifier = Modifier
            .addTarget(
                5,
                revealEffect = CircleRevealEffect(),
                backgroundCoachStyle = NoCoachMarkButtons,
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
                }
            )

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.phone__streamline_plump),
            contentDescription = "phone"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoachMarkScope.CoachMarkTopBar() {
    TopAppBar(title = {
        Text(
            "Compose Coach",
            modifier = Modifier.addTarget(
                position = 4,
                backgroundCoachStyle = CanopasStyle()
            ) {
                Text("Top Bar")
            }
        )
    })
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
