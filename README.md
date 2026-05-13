# 📝 Character Creator App

**Character Creator App** is a native Android application designed for tabletop RPG players (like D&D 5e) to streamline character creation and management. The app automates complex mechanics, including attribute-based skill calculations, level progressions, and equipment tracking, allowing players to focus on the story.

---

## Screenshots

| Dashboard | Character Stats | Skills & Logic |
| :---: | :---: | :---: |
| <img src="screenshots/main_screen.png" width="250" /> | <img src="screenshots/stats_screen.png" width="250" /> | <img src="screenshots/skills_screen.png" width="250" /> |

---

## Key Features
* **Automated Calculations:** Real-time computation of modifiers, saving throws, and skill bonuses based on character attributes.
* **Character Management:** Create, store, and manage multiple character profiles with ease.
* **Modern UI/UX:** Fully declarative interface built with **Jetpack Compose**, featuring smooth **Lottie** animations and **Material 3** design.
* **Cloud Sync & Auth:** Secure user authentication via **Firebase Auth** and real-time character data synchronization with **Firestore**. 
* **Offline-First & Persistence**: Robust data management using **Room** for local storage, ensuring 100% offline functionality with background cloud synchronization.

## Tech Stack
The project follows modern Android development standards and best practices:

* **Language:** [Kotlin](https://kotlinlang.org/) (Coroutines, StateFlow, Kotlinx Serialization)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* **Database:** [Room](https://developer.android.com/training/data-storage/room) (SQLite)
* **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & OkHttp
* **Architecture:** **MVVM** + **Clean Architecture** (Data, Domain, and UI layers)
* **Libraries:** Lottie Animations, Accompanist (System UI Controller), Navigation Compose, KSP.
* **Backend/Cloud:** Firebase (Authentication, Firestore Cloud Database).

## Architecture
This project implements **Clean Architecture** principles to ensure scalability and maintainability:
* **Data Layer:** Manages data coordination between the local Room database and Firebase Firestore using the Repository Pattern.
* **Domain Layer:** Contains pure business logic and Use Cases (e.g., `CalculateStatUseCase`).
* **UI Layer:** Built with Jetpack Compose, following the MVVM pattern to observe state via StateFlow.

## Installation & Setup
1. Clone the repository:
   ```bash
   git clone [https://github.com/mvoitovych/character-creator-app.git](https://github.com/mvoitovych/character-creator-app.git)
2. Firebase Configuration:
   **Create a Project:** Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
   **Register Android App:** * Click the Android icon to add an app.
   * Use your project's package name (found in `AndroidManifest.xml`).
   **Add Configuration File:** * Download the `google-services.json` file.
   * Place it in the following directory:
   ```text
   CharacterCreatorApp/
   └── app/
       └── google-services.json  <-- Place here
* **Enable Services:** In the Firebase Console, enable the following:
   * **Authentication:** Go to *Build > Authentication* and enable the **Email/Password** provider.
   * **Firestore Database:** Go to *Build > Firestore Database* and click **Create Database** (start in Test Mode).

3. **🚀 Build & Run:**
   * Open the project in **Android Studio**.
   * Wait for the IDE to index files, then click **Sync Project with Gradle Files** (the elephant icon 🐘).
   * Select your device or emulator and press **Run (Shift + F10)**.