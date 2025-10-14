# ComposeCoach 

ComposeCoach - Jetpack Compose
<div align="center">
<img src="https://github.com/vivekgupta4Git/ComposeCoach/assets/91813403/88358976-dc08-43a3-934c-505f4d145020" width=200 height=400 alt="Default"/>
</div>

## Overview
  ComposeCoach is a highly customizable library built in Jetpack Compose and Kotlin that allows users to easily integrate coachmarks into their Android applications. Coachmarks are a great way to guide users through the features of an app, making the onboarding experience more intuitive and engaging.

  This library is inspired by Canopas's [compose-intro-showcase](https://github.com/canopas/compose-intro-showcase) library mainly the 2.0.0 version and after draws inspiration from it, ComposeCoach introduces additional features and enhancements to provide a seamless and flexible experience for developers incorporating the best practices and lessons learned from the original implementation. What you can do with canopas's compose-intro-showcase library, you can do with this library and even more. 

## Features
  * Jetpack Compose Integration: ComposeCoach is built using Jetpack Compose, making it easy to integrate into modern Android applications.

  * Highly Customizable: The library provides a wide range of customization options, allowing developers to tailor the coachmarks to match the design and branding of their app. You can implement your own CoachStyle and Animation Style. 

 * Easy Implementation: With the help of provided samples, developers can quickly implement coachmarks within their applications without extensive coding efforts.

Inspired by compose-intro-showcase: The 2.0.0 version of CoachmarkLib is inspired by Canopas's ShowcaseIntro library, incorporating the best practices and lessons learned from the original implementation.

### Getting Started

To get started with ComposeCoach, follow these steps:

1. Add the CoachmarkLib dependency to your project.
```
dependencies {
	        implementation 'com.ruviapps:compose-coach:3.0.0'
	}
```
2. Create a state variable to show Coach Mark
```
 var showCoachMark by remember {
                mutableStateOf(true)
            }
```
3. Create a CoachMarkState
```             
            val coachMarkState = rememberCoachMarkState()
``` 
4. Wrap your Compose Content with CoachMarkHost and pass the state 
```
 CoachMarkHost(     showCoach = showCoachMark,
                    state = coachMarkState,
                    actions = object : DefaultCoachMarkActions(){
                        override fun onComplete() {
                            super.onComplete()
                            showCoachMark = false
                        }
                        override fun onSkip() {
                            super.onSkip()
                            showCoachMark = false
                        }
                    }
                ){
                    //your content goes here...
                }
```
5. Add composable to the coachmark by using addTarget Modifier
```
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
```
Customize the coachmarks according to your app's requirements.

**Implement coachmarks in your app and enhance the user onboarding experience.**

## Samples
The library includes several samples to help you quickly understand and implement Compose Coach in your project. Explore the samples to see how easy it is to integrate coachmarks into your app.

Version History
2.0.0:
Inspired by Canopas's ShowcaseIntro library.
Added additional features and enhancements for a more seamless experience.
Improved customization options.

## License
CoachmarkLib is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
Canopas for the inspiration drawn from compose-intro-showcase library.

## Contribution
We welcome contributions to ComposeCoach. If you have suggestions, feature requests, or bug reports, feel free to open an issue or create a pull request.

Happy coding!





