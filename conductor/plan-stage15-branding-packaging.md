# Plan - Stage 15: Branding and Packaging Refinements

## Objective
Finalize the MVP branding by ensuring consistent translations, creating a professional minimalist launcher icon (vector-based), and customizing the output APK filename.

## Scope & Impact
- **Translations:** Sync "Reserva / Investimentos" terminology across EN and ES string files.
- **Launcher Icon:** Generate a new `ic_launcher_foreground.xml` (SVG/Vector) with a minimalist financial theme.
- **Gradle:** Update app-level `build.gradle.kts` to rename the output APK.

## Implementation Steps

### 1. Consistently Translate Group Name
- Update `res/values-en/strings.xml`: `group_savings` -> "Savings / Investments".
- Update `res/values-es/strings.xml`: `group_savings` -> "Reserva / Inversiones".

### 2. Create Minimalist Launcher Icon
- I will generate a vector-based "Shield & Growth" icon in `app/src/main/res/drawable/ic_launcher_foreground.xml`.
- It will use the `EmeraldGreen` (#3C6939) and `PrimaryContainer` (#BCF0B4) from your custom palette.

### 3. Rename APK Output
- In `app/build.gradle.kts`, add a script in the `android` block to rename the output file to `BudgetFlow-${variant.versionName}.apk`.

## Verification
- Run `./gradlew assembleRelease` to check the new APK name.
- Verify strings in all 3 languages.
