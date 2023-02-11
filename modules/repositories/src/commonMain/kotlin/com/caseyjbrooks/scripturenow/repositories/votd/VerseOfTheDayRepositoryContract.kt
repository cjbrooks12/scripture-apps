package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached

public object VerseOfTheDayRepositoryContract {
    public data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public data class RefreshVerseOfTheDay(val forceRefresh: Boolean) : Inputs()
        public data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
    }

    public sealed class Events
}
