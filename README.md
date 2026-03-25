# BudgetFlow

BudgetFlow is an Android application for personal finance management, focused on helping users organize their monthly budget and track expenses in a simple and effective way.

This project is being developed as a learning-driven MVP using modern Android development practices and architecture.

---

## Features (MVP)

- Monthly income setup (salary + optional extra income)
- Budget planning based on customizable percentages (default 50/30/20)
- Expense tracking by categories
- Simple monthly dashboard:
    - Total income
    - Total expenses
    - Remaining balance
    - Budget usage by category
- Local data persistence (offline-first)

---

## Architecture

This project follows a **modern Android architecture**:

- MVVM (Model-View-ViewModel)
- Clean Architecture principles (simplified)
- Repository pattern
- Unidirectional Data Flow (UDF)

Layers:

- UI Layer (Jetpack Compose + ViewModel)
- Data Layer (Room, DataStore, Repositories)
- Domain Layer (optional, introduced as complexity grows)

---

## Tech Stack

- Kotlin
- Jetpack Compose
- ViewModel
- StateFlow
- Coroutines
- Room (local database)
- DataStore (preferences)
- Hilt (dependency injection)
- Navigation Compose

---

## Project Status

🚧 This project is currently under development (MVP phase)

---

## Goals

- Build a real-world Android app with clean and scalable architecture
- Apply best practices recommended by Google
- Learn and experiment with AI-assisted development (Gemini CLI / Codex CLI)
- Create a solid portfolio project

---

## Future Improvements

- Cloud sync (backend integration)
- Authentication
- Advanced analytics and charts
- Export data (PDF/CSV)
- Multi-device support

---

## Author

James Saboia