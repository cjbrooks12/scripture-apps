package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.utils.koinTest
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.Instant

public class GetPrayerByIdUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: String, text: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = text,
            prayerType = SavedPrayerType.Persistent,
            tags = listOf(),
            archived = false,
            archivedAt = null,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    "Get prayer by ID > Prayer not found" {
        koinTest {
            val useCase: GetPrayerByIdUseCase = get()

            val results = useCase(PrayerId("1")).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<SavedPrayer>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.FetchingFailed<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe null
        }
    }

    "Get prayer by ID > prayer found" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            val useCase: GetPrayerByIdUseCase = get()

            repository.createPrayer(getPrayer(0L, "1", "prayer one"))
            repository.createPrayer(getPrayer(0L, "2", "prayer one"))

            val results = useCase(PrayerId("2")).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<DailyPrayer>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<DailyPrayer>>()
            results[1].getCachedOrNull() shouldBe getPrayer(0L, "2", "prayer one")
        }
    }
})
