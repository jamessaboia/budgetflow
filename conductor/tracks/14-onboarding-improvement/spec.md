# Spec: Onboarding Improvement

## 1. Objective
Replace the static multi-step onboarding with a modern, visually engaging, and educational horizontal slider (Carousel). The goal is to onboard the user by explaining the app's philosophy (Monthly Planning vs. Cash Flow) and encouraging the use of educational hints before they enter their financial data.

## 2. Functional Requirements
- **Horizontal Slider (Pager):**
    - **Page 1: Welcome:** Focus on the "BudgetFlow" brand and the promise of financial organization.
    - **Page 2: Monthly Planning:** Explain that the user defines a "spending plan" which acts as a goal for the month.
    - **Page 3: Real Cash Flow:** Explain that the actual balance updates only with "Income" transactions (Income vs. Budget).
    - **Page 4: Learn as you go:** Introduce the "Info Icons" (Hints) as a tool for financial literacy tips.
- **Navigation Controls:**
    - Page indicators (dots) at the bottom.
    - "Next" and "Back" buttons (or a single primary button that changes text on the last page).
    - Skip option for returning users (though usually hidden for the very first run).
- **Smooth Transition to Data Entry:** After the slider, the user proceeds to the "Monthly Planning" (Income input) and "Percentage Selection" screens.

## 3. Technical Requirements
- **Components:** Use `androidx.compose.foundation.pager.HorizontalPager`.
- **Animations:** 
    - Use `AnimatedVisibility` for button changes.
    - Implement a "Pager Indicator" with smooth size/color transitions.
    - Add subtle entrance animations for text and icons on each page.
- **Assets:** Utilize existing Material Icons or custom Compose-drawn shapes to represent concepts (arrows, envelopes, lightbulbs).

## 4. UI/UX Considerations (Expert View)
- **Contrast:** Ensure high readability of text over the background.
- **Pacing:** The user shouldn't feel rushed. Use a "Get Started" button only on the final slide to ensure they see the educational content.
- **Micro-interactions:** Small scale or alpha changes as the user swipes between pages.

## 5. Acceptance Criteria
- [ ] Multi-page slider is functional and smooth.
- [ ] Educational content about Planning vs. Balance is clear.
- [ ] User is introduced to the "Hints" system.
- [ ] Final button leads to the existing Income/Planning data entry.
- [ ] `./gradlew build` passes.
