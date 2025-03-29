package com.example.noteapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteCategory
import com.example.noteapp.data.NoteDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NoteDatabase.getDatabase(application)
    private val noteDao = database.noteDao()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _selectedCategory = MutableStateFlow<NoteCategory?>(null)
    val selectedCategory: StateFlow<NoteCategory?> = _selectedCategory

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            noteDao.getAllNotes()
                .catch { e -> e.printStackTrace() }
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun setSelectedCategory(category: NoteCategory?) {
        _selectedCategory.value = category
        if (category != null) {
            loadNotesByCategory(category)
        } else {
            loadNotes()
        }
    }

    private fun loadNotesByCategory(category: NoteCategory) {
        viewModelScope.launch {
            noteDao.getNotesByCategory(category)
                .catch { e -> e.printStackTrace() }
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun addNote(title: String, content: String, category: NoteCategory) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                category = category
            )
            noteDao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun deleteNotesByCategory(category: NoteCategory) {
        viewModelScope.launch {
            noteDao.deleteNotesByCategory(category)
        }
    }
} 