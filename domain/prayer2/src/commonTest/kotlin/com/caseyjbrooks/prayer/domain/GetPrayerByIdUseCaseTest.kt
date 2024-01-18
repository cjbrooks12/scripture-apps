package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCaseImpl
import com.caseyjbrooks.prayer.utils.getPrayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

public class GetPrayerByIdUseCaseTest : StringSpec({
    "Get prayer by ID > Prayer not found" {
        val repository = InMemorySavedPrayersRepository()
        val useCase = GetPrayerByIdUseCaseImpl(repository)

        val results = useCase(PrayerId("1")).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<SavedPrayer>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.FetchingFailed<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe null
    }

    "Get prayer by ID > prayer found" {
        val repository = InMemorySavedPrayersRepository()
        val useCase = GetPrayerByIdUseCaseImpl(repository)

        repository.createPrayer(getPrayer("1", "prayer one"))
        repository.createPrayer(getPrayer("2", "prayer one"))

        val results = useCase(PrayerId("2")).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<DailyPrayer>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<DailyPrayer>>()
        results[1].getCachedOrNull() shouldBe getPrayer("2", "prayer one")
    }
})
