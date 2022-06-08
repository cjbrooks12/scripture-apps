package com.caseyjbrooks.app.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

@Composable
fun EditVerseForm(
    memoryVerse: MemoryVerse,
    updateMemoryVerse: (MemoryVerse) -> Unit,
) {
    var referenceText by remember { mutableStateOf(TextFieldValue(memoryVerse.reference)) }
    var verseContentText by remember { mutableStateOf(TextFieldValue(memoryVerse.text)) }

    LaunchedEffect(memoryVerse) {
        referenceText = referenceText.copy(text = memoryVerse.reference)
        verseContentText = verseContentText.copy(text = memoryVerse.text)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = referenceText,
            onValueChange = {
                referenceText = it
                updateMemoryVerse(
                    memoryVerse.copy(reference = referenceText.text)
                )
            },
            placeholder = { Text("Reference") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = verseContentText,
            onValueChange = {
                verseContentText = it
                updateMemoryVerse(
                    memoryVerse.copy(text = verseContentText.text)
                )
            },
            placeholder = { Text("Verse Text") }
        )
    }
}
