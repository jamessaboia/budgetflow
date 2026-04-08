# Plan: Branding & Animated Splash Evolution (Track 15)

## Step 1: Brand Design (Vector Assets)
- [ ] **Background:** Create `ic_launcher_background.xml` using a subtle gradient or solid primary color.
- [ ] **Foreground:** Create `ic_launcher_foreground.xml`. This will be the "Hero" asset:
    - Path data for a stylized 'B' that morphs into a growth arrow and a flow wave.
    - Ensure it fits within the "Safe Zone" of adaptive icons.
- [ ] **Logo Asset:** Create a dedicated `ic_logo.xml` for use inside the app (onboarding, splash).

## Step 2: Launcher Icon Implementation
- [ ] **Adaptive Icon:** Update `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` and `ic_launcher_round.xml`.
- [ ] **Legacy Support:** Generate or ensure fallback mipmaps are clean (though for MVP, adaptive icons are the focus).

## Step 3: Splash Screen Refinement
- [ ] **Splash Theme:** Update the Splash theme in `themes.xml` to use the new logo.
- [ ] **Exit Animation (MainActivity):**
    - Update the `setOnExitAnimationListener`.
    - Instead of just scaling down, implement a "Slide up + Fade" effect that mimics the Dashboard's Top Sheet entrance.
    - Orchestrate the background and the icon animations to be synchronized.

## Step 4: UI/UX Consistency
- [ ] **Theme Update:** Ensure `primaryLight` matches the logo's main green exactly.
- [ ] **App-wide Clean up:** Ensure no old icons remain in use.

## Step 5: Verification
- [ ] Run `./gradlew build`.
- [ ] Manual test: Verify icon on different launchers (different shapes).
- [ ] Manual test: Observe the splash transition into the Dashboard.
