package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.repository.daily.InMemoryDailyPrayerRepository
import com.caseyjbrooks.prayer.domain.getdaily.GetDailyPrayerUseCaseImpl
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.toList

public class GetDailyPrayerUseCaseTest : StringSpec({
    "Get daily prayer > fetch OK" {
        val useCase = GetDailyPrayerUseCaseImpl(
            InMemoryDailyPrayerRepository(
                DailyPrayer(
                    text = "Hardcoded Daily Prayer",
                    attribution = "Scripture Now!",
                    tags = listOf(PrayerTag("Hardcoded")),
                ),
            ),
        )

        val results = useCase().toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<DailyPrayer>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<DailyPrayer>>()
        results[1].getCachedOrNull() shouldBe DailyPrayer(
            text = "Hardcoded Daily Prayer",
            attribution = "Scripture Now!",
            tags = listOf(PrayerTag("Hardcoded")),
        )
    }

    "Get daily prayer > fetch failed" {
        val useCase = GetDailyPrayerUseCaseImpl(
            InMemoryDailyPrayerRepository(null),
        )

        val results = useCase().toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<DailyPrayer>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.FetchingFailed<DailyPrayer>>()
        results[1].getCachedOrNull() shouldBe null
    }
})
