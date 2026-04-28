package com.zeroknowledgeinteractive.codevault.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFormScreen(
    navController: NavController,
    viewModel: SnippetViewModel,
    snippetId: Int? = null
) {
    val languageOptions = listOf(
        "Kotlin",
        "Java",
        "JavaScript",
        "TypeScript",
        "Python",
        "C",
        "C++",
        "C#",
        "Go",
        "Rust",
        "Swift",
        "PHP",
        "SQL",
        "JSON",
        "XML",
        "HTML",
        "CSS",
        "Shell"
    )
    val snippets by viewModel.snippets.collectAsStateWithLifecycle()
    val editingSnippet = snippets.find { it.id == snippetId }
    val isEditMode = snippetId != null

    var title by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var languageExpanded by remember { mutableStateOf(false) }

    val canSave = title.isNotBlank() && code.isNotBlank() && language.isNotBlank()

    LaunchedEffect(editingSnippet?.id, isEditMode) {
        if (isEditMode) {
            title = editingSnippet?.title.orEmpty()
            code = editingSnippet?.code.orEmpty()
            description = editingSnippet?.description.orEmpty()
            language = editingSnippet?.language.orEmpty()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                TopAppBar(
                    title = { Text(if (isEditMode) "Edit snippet" else "New snippet") },
                    navigationIcon = {
                        TextButton(onClick = { navController.navigate(SNIPPET_LIST_SCREEN) }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isEditMode) "Editor session" else "New file",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (isEditMode) {
                            "Modify metadata and code, then save changes back to the workspace."
                        } else {
                            "Create a labeled snippet with consistent language metadata and a code-first layout."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = {}, label = { Text(if (isEditMode) "Editing" else "Creating") })
                        if (language.isNotBlank()) {
                            AssistChip(onClick = {}, label = { Text(language) })
                        }
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("File name") },
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = languageExpanded,
                        onExpandedChange = { languageExpanded = !languageExpanded }
                    ) {
                        OutlinedTextField(
                            value = language,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            label = { Text("Language mode") },
                            singleLine = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = languageExpanded,
                            onDismissRequest = { languageExpanded = false }
                        ) {
                            languageOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        language = option
                                        languageExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Notes") },
                        minLines = 3
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF111827)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0B1220))
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language.ifBlank { "plaintext" }.lowercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFAFC3DB)
                        )
                        Text(
                            text = "editor",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF74839A)
                        )
                    }

                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .padding(12.dp),
                        label = { Text("Code", color = Color(0xFF8FA2BB)) },
                        singleLine = false,
                        maxLines = 20,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFE5EDF7)
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2F6FED),
                            unfocusedBorderColor = Color(0xFF2A3547),
                            focusedContainerColor = Color(0xFF111827),
                            unfocusedContainerColor = Color(0xFF111827),
                            cursorColor = Color(0xFF99B8FF),
                            focusedTextColor = Color(0xFFE5EDF7),
                            unfocusedTextColor = Color(0xFFE5EDF7)
                        )
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val snippet = if (isEditMode && snippetId != null) {
                                Snippet(
                                    id = snippetId,
                                    title = title.trim(),
                                    code = code,
                                    description = description.trim(),
                                    language = language.trim()
                                )
                            } else {
                                Snippet(
                                    title = title.trim(),
                                    code = code,
                                    description = description.trim(),
                                    language = language.trim()
                                )
                            }

                            if (isEditMode) {
                                viewModel.update(snippet)
                            } else {
                                viewModel.insert(snippet)
                            }
                            navController.navigate(SNIPPET_LIST_SCREEN)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = canSave,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isEditMode) "Update snippet" else "Save snippet")
                    }

                    if (isEditMode && editingSnippet != null) {
                        TextButton(
                            onClick = {
                                viewModel.delete(editingSnippet)
                                navController.navigate(SNIPPET_LIST_SCREEN)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Delete snippet",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
