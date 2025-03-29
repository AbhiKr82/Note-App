package com.example.noteapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteCategory
import com.example.noteapp.ui.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

// Move noRippleClickable to be a regular extension function
fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = this.clickable(
    indication = null,
    interactionSource = interactionSource,
    onClick = onClick
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteId: Long,
    viewModel: NoteViewModel,
    onBackClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val note = notes.find { it.id == noteId }
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(note?.title ?: "") }
    var editedContent by remember { mutableStateOf(note?.content ?: "") }
    var editedCategory by remember { mutableStateOf(note?.category ?: NoteCategory.PERSONAL) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "View Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteConfirmation = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (note != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (isEditing) {
                    // Category Selection
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Select Category",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            NoteCategory.values().forEach { category ->
                                CategoryText(
                                    category = category,
                                    selected = editedCategory == category,
                                    onClick = { editedCategory = category }
                                )
                            }
                        }
                    }

                    // Title Input
                    OutlinedTextField(
                        value = editedTitle,
                        onValueChange = { editedTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = editedCategory.color,
                            focusedLabelColor = editedCategory.color
                        )
                    )

                    // Description Input
                    OutlinedTextField(
                        value = editedContent,
                        onValueChange = { editedContent = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = editedCategory.color,
                            focusedLabelColor = editedCategory.color
                        )
                    )

                    // Save Button
                    Button(
                        onClick = {
                            if (editedTitle.isNotBlank() && editedContent.isNotBlank()) {
                                viewModel.updateNote(
                                    note.copy(
                                        title = editedTitle,
                                        content = editedContent,
                                        category = editedCategory
                                    )
                                )
                                isEditing = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        enabled = editedTitle.isNotBlank() && editedContent.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = editedCategory.color
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    // View Mode
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        // Category
                        Text(
                            text = note.category.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = note.category.color,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Title
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Content
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Timestamp
                        Text(
                            text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                                .format(Date(note.timestamp)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            note?.let { viewModel.deleteNote(it) }
                            onBackClick()
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun CategoryText(
    category: NoteCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Remember the interaction source here
    val interactionSource = remember { MutableInteractionSource() }
    
    Text(
        text = category.name,
        style = MaterialTheme.typography.titleMedium,
        color = if (selected) category.color else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.noRippleClickable(
            interactionSource = interactionSource,
            onClick = onClick
        ),
        textAlign = TextAlign.Center
    )
} 