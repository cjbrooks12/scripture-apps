package com.caseyjbrooks.verses.screens.form

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.detail.VerseDetailRoute
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.datetime.Instant

internal object VerseFormContract {
    internal data class State(
        val verseId: VerseId?,
        val cachedVerse: Cached<SavedVerse> = Cached.NotLoaded(),
        val reference: String = "",
        val verseText: String = "",
        val completionDate: Instant? = null,
        val tags: Set<String> = emptySet()
    )

    internal sealed interface Inputs {
        data class ObserveVerse(val verseId: VerseId?) : Inputs
        data class VerseUpdated(val cachedVerses: Cached<SavedVerse>) : Inputs

        data class ReferenceUpdated(val text: String) : Inputs
        data class VerseTextUpdated(val text: String) : Inputs
        data class AddTag(val tag: String) : Inputs
        data class RemoveTag(val tag: String) : Inputs
        data object SaveVerse : Inputs

        /**
         * Navigate to the hierarchical parent of the [VerseDetailRoute], which is [VerseListRoute]
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
