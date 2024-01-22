package com.caseyjbrooks.prayer.repository.daily

import com.caseyjbrooks.prayer.models.DailyPrayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeDailyPrayerRepository(
    private val hardcodedPrayer: DailyPrayer?,
) : DailyPrayerRepository {
    override fun getTodaysDailyPrayer(): Flow<DailyPrayer?> {
        return flowOf(hardcodedPrayer)
    }
}
