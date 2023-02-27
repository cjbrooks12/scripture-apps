package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.copperleaf.ballast.repository.cache.Cached

public object VerseOfTheDayRepositoryContract {
    public data class State(
        val verseOfTheDayService: VerseOfTheDayService = VerseOfTheDayService.Default,
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public object Initialize : Inputs()
        public data class VerseOfTheDayServiceUpdated(val verseOfTheDayService: VerseOfTheDayService) : Inputs()
        public data class RefreshVerseOfTheDay(val forceRefresh: Boolean) : Inputs()
        public data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
    }

    public sealed class Events
}
