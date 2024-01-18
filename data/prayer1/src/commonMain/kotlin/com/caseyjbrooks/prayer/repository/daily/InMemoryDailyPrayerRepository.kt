package com.caseyjbrooks.prayer.repository.daily

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class InMemoryDailyPrayerRepository(
    private val hardcodedPrayer: DailyPrayer? = DailyPrayer(
        text = "Hardcoded Daily Prayer",
        attribution = "Scripture Now!",
        tags = listOf(PrayerTag("Hardcoded")),
    ),
) : DailyPrayerRepository {
    override fun getTodaysDailyPrayer(): Flow<DailyPrayer?> {
        return flowOf(hardcodedPrayer)
    }
}
