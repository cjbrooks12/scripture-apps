package com.caseyjbrooks.verses.screens.detail

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.copperleaf.ballast.repository.cache.Cached

internal object VerseDetailContract {
    internal data class State(
        val verseId: VerseId,
        val cachedVerse: Cached<SavedVerse> = Cached.NotLoaded(),
    )

    internal sealed interface Inputs {
        data class ObserveVerse(val verseId: VerseId) : Inputs
        data class VerseUpdated(val cachedVerses: Cached<SavedVerse>) : Inputs

        /**
         * Navigate to the hierarchical parent of the [VerseDetailRoute], which is [VerseListRoute]
         */
        data object NavigateUp : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object GoBack : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object Edit : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object Practice : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) : Events

        data object NavigateBack : Events
    }
}
