package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached

internal object ForYouDashboardContract {
    internal data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded()
    )

    internal sealed interface Inputs {
        data object Initialize : Inputs
        data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs
        data object VerseOfTheDayCardClicked : Inputs
    }

    internal sealed interface Events
}
