package com.caseyjbrooks.verses.screens.practice

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import kotlin.random.Random

internal object VersePracticeContract {
    internal data class State(
        val verseId: VerseId,
        val cachedVerse: Cached<SavedVerse> = Cached.NotLoaded(),
        val randomSeed: Long = 0L,
        val versePracticeMask: VersePracticeMask = VersePracticeMask.FirstLettersWithUnderscores(),
        val threshold: Float = 0F,
        val peeking: Boolean = false,
    ) {
        val maskedText = cachedVerse.map { verse ->
            if (peeking) {
                verse.text
            } else {
                versePracticeMask.applyMask(
                    Random(randomSeed),
                    verse.text,
                    threshold,
                )
            }
        }
    }

    internal sealed interface Inputs {
        data class ObserveVerse(val verseId: VerseId) : Inputs
        data class VerseUpdated(val cachedVerses: Cached<SavedVerse>) : Inputs
        data class UpdateThreshold(val threshold: Float) : Inputs
        data object TogglePeek : Inputs

        /**
         * Navigate to the hierarchical parent of the [VersePracticeRoute], which is [VerseListRoute]
         */
        data object NavigateUp : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) : Events

        data object NavigateBack : Events
    }
}
