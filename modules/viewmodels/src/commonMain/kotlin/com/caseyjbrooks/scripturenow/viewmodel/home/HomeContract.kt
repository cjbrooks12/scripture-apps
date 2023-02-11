package com.caseyjbrooks.scripturenow.viewmodel.home

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached

public object HomeContract {
    public data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
        val memoryVerse: Cached<MemoryVerse> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public object Initialize : Inputs()
        public data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs()
        public data class MemoryVerseUpdated(val memoryVerse: Cached<MemoryVerse>) : Inputs()
        public object VerseOfTheDayCardClicked : Inputs()
    }

    public sealed class Events {
        public object GoToVerseOfTheDay : Events()
    }
}
