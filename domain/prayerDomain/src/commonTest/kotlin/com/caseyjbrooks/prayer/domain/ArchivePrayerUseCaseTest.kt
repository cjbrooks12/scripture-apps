package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.utils.PrayerId
import com.caseyjbrooks.prayer.utils.TestClock
import com.caseyjbrooks.prayer.utils.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant

public class ArchivePrayerUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: Int): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = id.toString(),
            prayerType = SavedPrayerType.Persistent,
            tags = emptyList(),
            archived = false,
            archivedAt = if (false) instant else null,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    "Test Archive Prayer" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free)) {
            val clock: TestClock = get()
            val repository: SavedPrayersRepository = get()
            val useCase: ArchivePrayerUseCase = get()

            val initialPrayer = getPrayer(0L, 1)
            repository.createPrayer(initialPrayer)
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.archived shouldBe false
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.archivedAt.shouldBeNull()
            }

            // first call sets it to archives
            clock.advanceTime()
            useCase(initialPrayer)
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.archived shouldBe true
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(1L)
                prayer.archivedAt shouldBe Instant.fromEpochMilliseconds(1L)
            }

            // subsequent calls are a no-op. Timestamps should not be updated
            clock.advanceTime()
            useCase(initialPrayer)
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.archived shouldBe true
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(1L)
                prayer.archivedAt shouldBe Instant.fromEpochMilliseconds(1L)
            }
        }
    }
})
