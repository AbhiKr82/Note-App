# Your Personal Note

A modern, Material Design 3 note-taking application built with Kotlin and Jetpack Compose. This app provides a clean, 
intuitive interface for managing your personal notes with features like category organization and beautiful color-coding.

## Features

- Modern UI: Built with Material Design 3 and Jetpack Compose
- Category Management: Organize notes by categories with color coding
- Smooth Animations: Engaging user experience with fluid transitions and animations
- Search & Filter: Easily find notes by filtering through categories
- Clean Architecture: Built using MVVM architecture pattern
- Local Storage: Secure local storage using Room Database
- Dark Mode Support: Automatic theme adaptation based on system settings

## Tech Stack

- Language: Kotlin
- UI Framework: Jetpack Compose
- Architecture: MVVM (Model-View-ViewModel)
- Database: Room

## Project Structure

```
app/
├── data/
│   ├── Note.kt
│   └── NoteCategory.kt
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── AddNoteScreen.kt
│   │   ├── NoteScreen.kt
│   │   └── SplashScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── NoteViewModel.kt
└── MainActivity.kt
```

