# Plan - Stage 2: Setup Core & Dependency Injection (Marco 1)

## Objective
Configure the foundational project setup for the BudgetFlow app. This includes managing all necessary dependencies (Hilt, Room, DataStore, Navigation Compose) via the Version Catalog, setting up Gradle plugins (including KSP), creating the base package structure, and configuring the custom `Application` class for Hilt.

## Scope & Impact
- Updates `gradle/libs.versions.toml` to include the latest 2026 stable versions of required libraries.
- Modifies `build.gradle.kts` (project level) to define required plugins (KSP, Hilt, Kotlin Android).
- Modifies `app/build.gradle.kts` to apply plugins, enable KSP, and declare dependencies.
- Modifies `app/src/main/AndroidManifest.xml` to point to the new custom Application class.
- Scaffolds the fundamental directory structure for Clean Architecture.

## Implementation Steps

### 1. Update Version Catalog (`gradle/libs.versions.toml`)
Add the following versions, libraries, and plugins:
- **Versions**: `hilt` (2.59.2), `room` (2.8.4), `datastore` (1.2.1), `navigationCompose` (2.9.7), `ksp` (2.3.6), `hiltNavigationCompose` (1.3.0).
- **Libraries**:
    - `hilt-android`, `hilt-compiler`, `hilt-navigation-compose`
    - `room-runtime`, `room-ktx`, `room-compiler`
    - `datastore-preferences`
    - `navigation-compose`
- **Plugins**: `kotlin-android`, `hilt`, `ksp`

### 2. Update Project-Level `build.gradle.kts`
Define the plugins for `hilt`, `ksp`, and `kotlin-android` with `apply false`.

### 3. Update App-Level `app/build.gradle.kts`
- Apply `com.google.dagger.hilt.android` and `com.google.devtools.ksp` plugins.
- Add implementation and ksp dependencies for Hilt, Room, DataStore, and Navigation.

### 4. Create Package Structure & Base Application
- Scaffold the `com.jamessaboia.budgetflow` structure: `di/`, `core/`, `navigation/`, `data/`, `domain/`, `ui/`.
- Create `BudgetFlowApplication.kt` inside the root package.
- Annotate `BudgetFlowApplication` with `@HiltAndroidApp`.

### 5. Update AndroidManifest.xml
- Update `<application>` tag to include `android:name=".BudgetFlowApplication"`.

## Verification & Testing
- Run `./gradlew build` to ensure all dependencies resolve correctly.
- Verify that the KSP plugin executes without errors with the Kotlin compiler.
- Run the app (debug) to ensure the application starts without Dagger/Hilt crashes.