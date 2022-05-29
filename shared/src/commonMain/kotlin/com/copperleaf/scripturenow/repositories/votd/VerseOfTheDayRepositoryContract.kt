package com.copperleaf.scripturenow.repositories.votd

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay

object VerseOfTheDayRepositoryContract {
    data class State(
        val initialized: Boolean = false,

        val verseOfTheDayInitialized: Boolean = false,
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object ClearCaches : Inputs()
        object Initialize : Inputs()
        object RefreshAllCaches : Inputs()

        data class RefreshVerseOfTheDay(val forceRefresh: Boolean) : Inputs()
        data class DataListUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
    }
}
