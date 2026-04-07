# Plan: Onboarding Improvement (Track 14)

## Step 1: Resource Preparation (I18n)
- [ ] **Strings Update:** Add content for the 4 slider pages in PT, EN, and ES.
    - Intro Title/Desc.
    - Planning Concept Title/Desc.
    - Cash Flow Concept Title/Desc.
    - Hints Education Title/Desc.
    - Button labels: "Next", "Back", "Get Started".

## Step 2: Architecture & State
- [ ] **OnboardingStep Update:** Add an `INTRO_SLIDER` step to the `OnboardingStep` enum in `OnboardingUiState.kt`.
- [ ] **Pager State:** Initialize `rememberPagerState` in `OnboardingScreen.kt`.

## Step 3: UI Components Implementation
- [ ] **Pager Indicator:** Create a custom animated dot indicator.
- [ ] **IntroSlider Component:** Implement the `HorizontalPager` container.
- [ ] **Slider Pages:** Create a factory or individual composables for each page:
    - `WelcomePage`
    - `PlanningConceptPage`
    - `CashFlowPage`
    - `HintsEducationPage`

## Step 4: Refinement & Animations (Expert Mode)
- [ ] **Transition Logic:** Ensure the primary button only moves to the data entry phase (`INCOME` step) after the last slide.
- [ ] **Entrance Animations:** Use `AnimatedVisibility` for text and icons within each page to create a "reveal" effect as the user swipes.
- [ ] **Visual Assets:** Draw or use icons with meaningful scales (e.g., larger icons for focus).

## Step 5: Integration
- [ ] **ViewModel Logic:** Update `OnboardingViewModel` to handle the transition from the slider to the data entry steps.
- [ ] **Navigation Check:** Ensure that completing the slider marks progress correctly in the internal onboarding flow.

## Step 6: Verification
- [ ] Run all existing unit tests to ensure no regressions.
- [ ] Run `./gradlew build`.
- [ ] Manual walkthrough: Verify the flow from app open -> Slider -> Data Entry -> Dashboard.
