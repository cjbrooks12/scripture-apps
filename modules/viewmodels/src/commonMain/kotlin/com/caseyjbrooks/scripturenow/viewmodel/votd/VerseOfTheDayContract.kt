package com.caseyjbrooks.scripturenow.viewmodel.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached

public object VerseOfTheDayContract {
    public data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
        val saved: Boolean = false,
    )

    public sealed class Inputs {
        public data class Initialize(val forceRefresh: Boolean) : Inputs()
        public data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
        public object SaveAsMemoryVerse : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public object NavigateUp : Events()
    }
}
