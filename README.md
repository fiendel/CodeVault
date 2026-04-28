# CodeVault

CodeVault is a modern Android application designed for developers to store, organize, and quickly access their code snippets. Built with the latest Android technologies, it provides a clean, "cute," and efficient workspace for your personal code library.

## Features

- **Snippet Management**: Easily add, edit, and view your code snippets with syntax-specific labels.
- **Persistent Storage**: All snippets are saved locally using **Room Database**, ensuring your data is always available offline.
- **Smart Organization**: snippets include titles, descriptions, and language tags for easy identification.
- **Modern UI**: A polished Material 3 interface featuring elevated cards, monospace code previews, and smooth transitions.
- **Theme Customization**: Full support for Dark and Light modes, with a persistent setting saved via **Jetpack DataStore**.
- **System Integration**: Option to follow system theme or manually override it.

## Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
- **Database**: [Room](https://developer.android.com/training/data-storage/room).
- **Persistence**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore).
- **Architecture**: MVVM (ViewModel, Repository, DAO).
- **Language**: Kotlin.

## Getting Started

1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Build and run on an emulator or physical device (Min SDK 26).

## Screens

- **Snippet List**: Your main workspace showing all indexed snippets with quick previews.
- **Add/Edit Form**: A simple interface to input your snippet details.
- **Snippet Detail**: A focused view to read and copy your code.
- **Settings**: Customize your experience with theme toggles.

---
*Developed with focus on simplicity and developer experience.*
