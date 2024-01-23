package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.repository.daily.DailyPrayerRepository
import com.caseyjbrooks.prayer.repository.daily.FakeDailyPrayerRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList

public class FakeDailyPrayerRepositoryTest : StringSpec({
    "test" {
        val repository: DailyPrayerRepository = FakeDailyPrayerRepository(
            DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = listOf(PrayerTag("Hardcoded")),
            )
        )

        val responseList = repository.getTodaysDailyPrayer().toList()

        responseList shouldBe listOf(
            DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = listOf(PrayerTag("Hardcoded")),
            ),
        )
    }
})
