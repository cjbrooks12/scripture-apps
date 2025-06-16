package com.copperleaf.abide.minigames.verses.typeverse

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun TypeVerseWithDashesMinigame(
    verseReference: String,
    verseText: String,
    onCompleted: (TypeVerseMinigameResult) -> Unit,
) {
    Column {
        val text = rememberTextFieldState()

        val maxProgress = remember { getWords(verseText).size }
        var progress by remember { mutableStateOf(0) }
        val verseHint by derivedStateOf {
            reformatText(
                text = verseText,
                reformatWord = { CharArray(it.length) { '_' }.concatToString() },
                random = Random(1),
                threshold = 0.5f,
                progress = progress
            )
        }

        Text("Type out the verse using the dashes as a guide", style = MaterialTheme.typography.headlineMedium)
        Text(verseReference, style = MaterialTheme.typography.labelLarge)
        Text(verseHint, style = MaterialTheme.typography.bodyLarge)

        BasicTextField(
            text,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp)),
        )

        Button(onClick = {
            if (progress == maxProgress) {
                onCompleted(checkVerse(text.text.toString(), verseText))
            } else {
                progress++
            }
        }) {
            Text("Check")
        }
    }
}


