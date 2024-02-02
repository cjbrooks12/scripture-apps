package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached

internal object ForYouDashboardContract {
    internal data class State(
        val verseOfTheDay: Cached<VerseOfTheDay> = Cached.NotLoaded(),
        val dailyPrayer: Cached<DailyPrayer> = Cached.NotLoaded(),

        val showOverview: Boolean = true,
        val showVerseOfTheDay: Boolean = true,
        val showDailyPrayer: Boolean = true,

        val noticeText: String? = null,
    )

    internal sealed interface Inputs {
        data object Initialize : Inputs

        data object OverviewCardClicked : Inputs

        data class VerseOfTheDayUpdated(val verseOfTheDay: Cached<VerseOfTheDay>) : Inputs
        data object VerseOfTheDayCardClicked : Inputs

        data class DailyPrayerUpdated(val dailyPrayer: Cached<DailyPrayer>) : Inputs
        data object DailyPrayerCardClicked : Inputs

        data class NoticeTextUpdated(val noticeText: String?) : Inputs
        data object NoticeCardClicked : Inputs
    }

    internal sealed interface Events
}
