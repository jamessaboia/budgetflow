# Tech Stack - BudgetFlow

## Core Stack
- **Language:** [Kotlin](https://kotlinlang.org/) (Coroutines, StateFlow, Serialization)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM + Simplified Clean Architecture
- **Inversion of Control:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (Dagger-based DI)

## Persistence
- **Relational DB:** [Room](https://developer.android.com/training/data-storage/room) (Transactions, Budgets, Categories)
- **Key-Value Store:** [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (User Preferences, Onboarding status)

## Navigation
- **Library:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Type Safety:** Type-safe Navigation (available in modern Compose Nav)

## Architecture Layers
- **UI Layer:** Compose Screens, ViewModels, UI State.
- **Domain Layer:** Use Cases (business logic), Domain Models, Repository Interfaces.
- **Data Layer:** Repositories (impl), Local Sources (DAO, DataStore), Data Models.

## Dependencies (Key Versions)
- **Compose Compiler:** Kotlin 2.x
- **Material 3:** Compose Material 3
- **Room:** 2.6+
- **Hilt:** 2.5x+
- **DataStore:** 1.1.x+
