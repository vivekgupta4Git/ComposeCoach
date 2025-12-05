
[![Maven Central](https://img.shields.io/badge/Maven%20Central-compose--coach-blue)](https://central.sonatype.com/artifact/com.ruviapps.coachmark/compose-coach)
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<img alt="Kotlin Multiplatform" src="https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet"/>
<img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose-Multiplatform-46a2f1"/>

# ComposeCoach

ComposeCoach — A lightweight Kotlin Multiplatform (KMP) library for Coach Marks

<div align="center">


https://github.com/user-attachments/assets/e2d8f597-3231-432b-b8f6-329c06254ba9


</div>

## Overview
ComposeCoach is a highly customizable KMP library built with Jetpack Compose Multiplatform that allows developers to easily add coach marks and guided user tours across **Android, iOS, Desktop, and Web**.

Coachmarks help highlight UI components and guide users during onboarding or feature discovery.

## ✨ Features

### ✔ Kotlin Multiplatform Support  
Runs seamlessly on:  
- **Android**  
- **iOS**  
- **Desktop (JVM)**  
- **Web (Compose HTML)**  

### ✔ Jetpack Compose / Compose Multiplatform Integration  
Built entirely with modern declarative UI APIs.

### ✔ Fully Customizable  
Implement your own:
- Coach styles  
- Button layouts  
- Reveal animations  
- Target shapes  

### ✔ Simple to Use  
Just wrap your content and mark UI elements using `addTarget`.

---

## 🚀 Getting Started

Add the dependency:

<details>
<summary><b>Android Project</b></summary>

```gradle
dependencies {
    implementation "com.ruviapps.coachmark:compose-coach-android:<latest-version>"
}
```
</details>

<details>
<summary><b>Kotlin Multiplatform Project</b></summary>

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.ruviapps.coachmark:compose-coach:<latest_version>")
            }
        }
    }
}
```
</details>

---

## 📌 Usage Example

### 1. State variable
```kotlin
var showCoachMark by remember { mutableStateOf(true) }
```

### 2. Create a CoachMarkState
```kotlin
val coachMarkState = rememberCoachMarkState()
```

### 3. Wrap content using CoachMarkHost
```kotlin
CoachMarkHost(
    showCoach = showCoachMark,
    state = coachMarkState,
    actions = object : DefaultCoachMarkActions() {
        override fun onComplete() { showCoachMark = false }
        override fun onSkip() { showCoachMark = false }
    }
) {
    // Your app UI…
}
```

### 4. Add targets using `addTarget`
```kotlin
Text(
    "Compose Coach",
    modifier = Modifier.addTarget(
        position = 2,
        revealEffect = CircleRevealEffect(),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "A Highly Customizable Coach Mark Library!!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        backgroundCoachStyle = NoCoachMarkButtons
    )
)
```

---

## 📄 License  
ComposeCoach is licensed under the **MIT License**.

---

## 🤝 Contribution  
Contributions are welcome!  
Feel free to open issues or submit PRs.

**Happy coding!**
