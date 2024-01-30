package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.repository.settings.PrayerUserSettings
import com.caseyjbrooks.prayer.utils.PrayerId
import com.caseyjbrooks.prayer.utils.TestClock
import com.caseyjbrooks.prayer.utils.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant

public class SaveDailyPrayerUseCaseTest : StringSpec({
    "Test Save Daily Prayer > all tags" {
        koinTest(
            prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
            dailyPrayer = DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = listOf(PrayerTag("Hardcoded")),
            )
        ) {
            val clock = TestClock()
            val savedPrayersRepository: SavedPrayersRepository = get()
            val useCase: SaveDailyPrayerUseCase = get()

            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe emptyList()

            // add the daily verse to the user's collection
            useCase()
            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe listOf(
                SavedPrayer(
                    uuid = PrayerId(1),
                    text = "Hardcoded Daily Prayer",
                    prayerType = SavedPrayerType.Persistent,
                    tags = listOf(PrayerTag("Daily Prayer"), PrayerTag("Hardcoded")),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            )

            // don't add a new verse, don't update the existing one. It's already there
            clock.advanceTime()
            useCase()
            savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe listOf(
                SavedPrayer(
                    uuid = PrayerId(1),
                    text = "Hardcoded Daily Prayer",
                    prayerType = SavedPrayerType.Persistent,
                    tags = listOf(PrayerTag("Daily Prayer"), PrayerTag("Hardcoded")),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            )
        }
    }

    "Test Save Daily Prayer > default tag" {
        koinTest(
            prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
            dailyPrayer = DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = emptyList(),
            )
        ) {
            val savedPrayersRepository: SavedPrayersRepository = get()
            val useCase: SaveDailyPrayerUseCase = get()

            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe emptyList()

            // add the daily verse to the user's collection
            useCase()
            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe listOf(
                SavedPrayer(
                    uuid = PrayerId(1),
                    text = "Hardcoded Daily Prayer",
                    prayerType = SavedPrayerType.Persistent,
                    tags = listOf(PrayerTag("Daily Prayer")),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            )
        }
    }

    "Test Save Daily Prayer > tag from API" {
        koinTest(
            prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
            dailyPrayer = DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = listOf(PrayerTag("Hardcoded")),
            )
        ) {
            val prayerUserSettings: PrayerUserSettings = get()
            prayerUserSettings.addDefaultTagToSavedDailyPrayer = false
            val savedPrayersRepository: SavedPrayersRepository = get()
            val useCase: SaveDailyPrayerUseCase = get()

            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe emptyList()

            // add the daily verse to the user's collection
            useCase()
            savedPrayersRepository
                .getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe listOf(
                SavedPrayer(
                    uuid = PrayerId(1),
                    text = "Hardcoded Daily Prayer",
                    prayerType = SavedPrayerType.Persistent,
                    tags = listOf(PrayerTag("Hardcoded")),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            )
        }
    }

    "Test Save Daily Prayer > no tags" {
        koinTest(
            prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
            dailyPrayer = DailyPrayer(
                text = "Hardcoded Daily Prayer",
                attribution = "Scripture Now!",
                tags = listOf(PrayerTag("Hardcoded")),
            )
        ) {
            val savedPrayersRepository: SavedPrayersRepository = get()
            val prayerUserSettings: PrayerUserSettings = get()
            prayerUserSettings.saveTagsFromDailyPrayer = false
            prayerUserSettings.addDefaultTagToSavedDailyPrayer = false
            val useCase: SaveDailyPrayerUseCase = get()

            savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe emptyList()

            // add the daily verse to the user's collection
            useCase()
            savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet())
                .first() shouldBe listOf(
                SavedPrayer(
                    uuid = PrayerId(1),
                    text = "Hardcoded Daily Prayer",
                    prayerType = SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            )
        }
    }
})
