# BudgetFlow

## Project Overview
BudgetFlow is an Android application built natively using Kotlin. It adopts a modern Android architecture and toolset, specifically leveraging Jetpack Compose as its declarative UI framework and the Material 3 design system for styling. The project's build and dependency management is handled by Gradle using Kotlin DSL (`.kts` files) and Gradle Version Catalogs (`libs.versions.toml`).

## Technologies and Architecture
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 36
- **Build System:** Gradle (Kotlin DSL)
- **Dependency Management:** Gradle Version Catalogs
- **Testing:** JUnit4 (Unit Testing), Espresso & Compose UI Test (Instrumentation Testing)

## Building and Running
The project includes a standard Gradle wrapper. You do not need a local Gradle installation to run these commands:
- **Build the project:** `./gradlew build`
- **Run the app (debug):** `./gradlew installDebug` (or run it via Android Studio directly)
- **Run Unit Tests:** `./gradlew test`
- **Run UI/Instrumentation Tests:** `./gradlew connectedAndroidTest`
- **Clean the project:** `./gradlew clean`

## Development Conventions
- **Dependency Management:** All libraries, plugins, and their respective versions are centrally managed in `gradle/libs.versions.toml`. When adding a new dependency, define it in the version catalog first, then reference it in the `build.gradle.kts` files using the `libs.*` notation.
- **UI Development:** UI is built entirely with Jetpack Compose. Compose best practices apply: keep Composables stateless where possible by hoisting state, use Modifiers effectively, and always include `@Preview` annotations for UI components to verify visual layout and accelerate development.
- **Code Style:** Standard Kotlin coding conventions should be followed.
