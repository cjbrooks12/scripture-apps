package com.copperleaf.scripturenow.ui.votd

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay

object VotdContract {
    data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded()
    )

    sealed class Inputs {
        data class Initialize(val forceRefresh: Boolean) : Inputs()
        data class VerseOfTheDayChanged(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object NavigateUp : Events()
    }
}
