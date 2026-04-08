# Spec: Branding & Animated Splash Evolution

## 1. Objective
Redesign the app's visual identity starting from its core: the launcher icon and the splash screen experience. The goal is to move away from generic/placeholder assets to a premium, custom-designed vector brand that represents "Financial Flow" and "Security".

## 2. Visual Identity Concept
- **Logo:** A minimalist vector mark combining a "Flow" wave with a "Growth" arrow.
- **Colors:** Deep use of Primary Green (#3C6939) with subtle gradients.
- **Icon Type:** Adaptive Icon (supporting all Android shapes) with a distinct foreground and background.

## 3. Functional Requirements
- **Launcher Icon:**
    - New vector foreground (`ic_launcher_foreground.xml`).
    - Standardized background (`ic_launcher_background.xml`).
    - Support for Adaptive Icons (standard and round).
- **Animated Splash Screen:**
    - Refine the `androidx.core:core-splashscreen` implementation.
    - Implement a custom exit animation: The logo should not just fade, but "flow" or scale up elegantly to reveal the Dashboard.

## 4. UI/UX Considerations (Expert View)
- **Continuity:** The splash screen background should match the Dashboard's Top Sheet color to create a seamless "entrance" effect.
- **Simplicity:** A professional logo must be legible even at small sizes (notification bar).

## 5. Acceptance Criteria
- [ ] New vector logo is implemented as the app's launcher icon.
- [ ] Adaptive icon properties are correctly configured.
- [ ] Splash screen exit animation is smooth and professional.
- [ ] Brand colors are consistent across the app.
- [ ] `./gradlew build` passes.
