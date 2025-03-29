package com.example.noteapp.data

import androidx.compose.ui.graphics.Color

enum class NoteCategory(val color: Color) {
    WORK(Color(0xFF4CAF50)),        // Green
    STUDY(Color(0xFF2196F3)),       // Blue
    PERSONAL(Color(0xFF9C27B0)),    // Purple
    OTHERS(Color(0xFF607D8B))       // Grey
} 