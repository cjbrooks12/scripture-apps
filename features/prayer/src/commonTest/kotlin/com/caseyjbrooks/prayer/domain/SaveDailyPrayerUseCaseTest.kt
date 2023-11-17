package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCase
import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCaseImpl
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.config.InMemoryPrayerConfig
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import com.caseyjbrooks.prayer.repository.daily.DailyPrayerRepository
import com.caseyjbrooks.prayer.repository.daily.InMemoryDailyPrayerRepository
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.repository.settings.InMemoryPrayerUserSettings
import com.caseyjbrooks.prayer.repository.settings.PrayerUserSettings
import com.caseyjbrooks.prayer.repository.user.InMemoryPrayerUserRepository
import com.caseyjbrooks.prayer.repository.user.PrayerUserRepository
import com.caseyjbrooks.prayer.utils.TestClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant

public class SaveDailyPrayerUseCaseTest : StringSpec({
    "Test Save Daily Prayer > all tags" {
        val clock = TestClock()
        val savedPrayersRepository: SavedPrayersRepository = InMemorySavedPrayersRepository()
        val dailyPrayerRepository: DailyPrayerRepository = InMemoryDailyPrayerRepository()
        val prayerUserRepository: PrayerUserRepository = InMemoryPrayerUserRepository(
            PrayerUser("user one", PrayerUser.SubscriptionStatus.Free),
        )
        val prayerUserSettings: PrayerUserSettings = InMemoryPrayerUserSettings()
        val prayerConfig: PrayerConfig = InMemoryPrayerConfig()
        val useCase: SaveDailyPrayerUseCase = SaveDailyPrayerUseCaseImpl(
            savedPrayersRepository = savedPrayersRepository,
            dailyPrayerRepository = dailyPrayerRepository,
            prayerUserSettings = prayerUserSettings,
            createPrayerUseCase = CreatePrayerUseCaseImpl(
                savedPrayersRepository = savedPrayersRepository,
                prayerConfig = prayerConfig,
                prayerUserRepository = prayerUserRepository,
                clock = clock,
            ),
            uuidFactory = { "1" },
            clock = clock,
        )

        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe emptyList()

        // add the daily verse to the user's collection
        useCase()
        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe listOf(
            SavedPrayer(
                uuid = PrayerId("1"),
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
        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe listOf(
            SavedPrayer(
                uuid = PrayerId("1"),
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

    "Test Save Daily Prayer > default tag" {
        val clock = TestClock()
        val savedPrayersRepository: SavedPrayersRepository = InMemorySavedPrayersRepository()
        val dailyPrayerRepository: DailyPrayerRepository = InMemoryDailyPrayerRepository()
        val prayerUserRepository: PrayerUserRepository = InMemoryPrayerUserRepository(
            PrayerUser("user one", PrayerUser.SubscriptionStatus.Free),
        )
        val prayerUserSettings: PrayerUserSettings = InMemoryPrayerUserSettings(saveTagsFromDailyPrayer = false)
        val prayerConfig: PrayerConfig = InMemoryPrayerConfig()
        val useCase: SaveDailyPrayerUseCase = SaveDailyPrayerUseCaseImpl(
            savedPrayersRepository = savedPrayersRepository,
            dailyPrayerRepository = dailyPrayerRepository,
            prayerUserSettings = prayerUserSettings,
            createPrayerUseCase = CreatePrayerUseCaseImpl(
                savedPrayersRepository = savedPrayersRepository,
                prayerConfig = prayerConfig,
                prayerUserRepository = prayerUserRepository,
                clock = clock,
            ),
            uuidFactory = { "1" },
            clock = clock,
        )

        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe emptyList()

        // add the daily verse to the user's collection
        useCase()
        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe listOf(
            SavedPrayer(
                uuid = PrayerId("1"),
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

    "Test Save Daily Prayer > tag from API" {
        val clock = TestClock()
        val savedPrayersRepository: SavedPrayersRepository = InMemorySavedPrayersRepository()
        val dailyPrayerRepository: DailyPrayerRepository = InMemoryDailyPrayerRepository()
        val prayerUserRepository: PrayerUserRepository = InMemoryPrayerUserRepository(
            PrayerUser("user one", PrayerUser.SubscriptionStatus.Free),
        )
        val prayerUserSettings: PrayerUserSettings = InMemoryPrayerUserSettings(addDefaultTagToSavedDailyPrayer = false)
        val prayerConfig: PrayerConfig = InMemoryPrayerConfig()
        val useCase: SaveDailyPrayerUseCase = SaveDailyPrayerUseCaseImpl(
            savedPrayersRepository = savedPrayersRepository,
            dailyPrayerRepository = dailyPrayerRepository,
            prayerUserSettings = prayerUserSettings,
            createPrayerUseCase = CreatePrayerUseCaseImpl(
                savedPrayersRepository = savedPrayersRepository,
                prayerConfig = prayerConfig,
                prayerUserRepository = prayerUserRepository,
                clock = clock,
            ),
            uuidFactory = { "1" },
            clock = clock,
        )

        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe emptyList()

        // add the daily verse to the user's collection
        useCase()
        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe listOf(
            SavedPrayer(
                uuid = PrayerId("1"),
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

    "Test Save Daily Prayer > no tags" {
        val clock = TestClock()
        val savedPrayersRepository: SavedPrayersRepository = InMemorySavedPrayersRepository()
        val dailyPrayerRepository: DailyPrayerRepository = InMemoryDailyPrayerRepository()
        val prayerUserRepository: PrayerUserRepository = InMemoryPrayerUserRepository(
            PrayerUser("user one", PrayerUser.SubscriptionStatus.Free),
        )
        val prayerUserSettings: PrayerUserSettings = InMemoryPrayerUserSettings(
            saveTagsFromDailyPrayer = false,
            addDefaultTagToSavedDailyPrayer = false,
        )
        val prayerConfig: PrayerConfig = InMemoryPrayerConfig()
        val useCase: SaveDailyPrayerUseCase = SaveDailyPrayerUseCaseImpl(
            savedPrayersRepository = savedPrayersRepository,
            dailyPrayerRepository = dailyPrayerRepository,
            prayerUserSettings = prayerUserSettings,
            createPrayerUseCase = CreatePrayerUseCaseImpl(
                savedPrayersRepository = savedPrayersRepository,
                prayerConfig = prayerConfig,
                prayerUserRepository = prayerUserRepository,
                clock = clock,
            ),
            uuidFactory = { "1" },
            clock = clock,
        )

        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe emptyList()

        // add the daily verse to the user's collection
        useCase()
        savedPrayersRepository.getPrayers(ArchiveStatus.FullCollection, emptyList()).first() shouldBe listOf(
            SavedPrayer(
                uuid = PrayerId("1"),
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
})
