package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
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

public class UpdatePrayerUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: Int, text: String): SavedPrayer {
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

    "Test Update Prayer" {
        koinTest {
            val clock: TestClock = get()
            val repository: SavedPrayersRepository = get()
            val useCase: UpdatePrayerUseCase = get()

            val initialPrayer = getPrayer(0L, 1, "initial text")
            repository.createPrayer(initialPrayer)
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.text shouldBe "initial text"
                prayer.archived shouldBe false
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.archivedAt.shouldBeNull()
            }

            // first call sets it to archives
            clock.advanceTime()
            val updatedPrayer = useCase(initialPrayer.copy(text = "updated text"))
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.text shouldBe "updated text"
                prayer.archived shouldBe false
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(1L)
                prayer.archivedAt.shouldBeNull()
            }

            // subsequent calls are a no-op. Timestamps should not be updated
            clock.advanceTime()
            useCase(updatedPrayer)
            repository.getPrayerById(initialPrayer.uuid).first().let { prayer ->
                prayer.shouldNotBeNull()
                prayer.uuid shouldBe PrayerId(1)
                prayer.text shouldBe "updated text"
                prayer.archived shouldBe false
                prayer.createdAt shouldBe Instant.fromEpochMilliseconds(0L)
                prayer.updatedAt shouldBe Instant.fromEpochMilliseconds(1L)
                prayer.archivedAt.shouldBeNull()
            }
        }
    }
})
