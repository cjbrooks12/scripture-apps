package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.autoarchive.AutoArchivePrayersUseCase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.utils.TestClock
import com.caseyjbrooks.prayer.utils.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds

public class AutoArchivePrayersUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: String, archived: Boolean, vararg tags: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = id,
            prayerType = SavedPrayerType.Persistent,
            tags = tags.map { PrayerTag(it) },
            archived = archived,
            archivedAt = if (archived) instant else null,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    "Test auto-completing scheduled prayers" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free)) {
            val clock: TestClock = get()
            val repository: SavedPrayersRepository = get()
            val useCase: AutoArchivePrayersUseCase = get()
            val timeZone: TimeZone = get()

            val initialInstant = clock.now().toLocalDateTime(timeZone).toInstant(timeZone)

            listOf(
                getPrayer(0L, "1", false),
                getPrayer(0L, "2", false),
                getPrayer(0L, "3", true),
                SavedPrayer(
                    uuid = PrayerId("4"),
                    text = "4",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
                SavedPrayer(
                    uuid = PrayerId("5"),
                    text = "5",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
                SavedPrayer(
                    uuid = PrayerId("6"),
                    text = "6",
                    prayerType = SavedPrayerType.ScheduledCompletable(
                        initialInstant.plus(10.milliseconds).toLocalDateTime(timeZone),
                    ),
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
            ).map { repository.createPrayer(it) }

            // the first time we run it, nothing happens since the scheduled time has not passed
            clock.advanceTimeBy(1.milliseconds)
            useCase()
            repository.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet()).first() shouldBe listOf(
                getPrayer(0L, "1", false),
                getPrayer(0L, "2", false),
                getPrayer(0L, "3", true),
                SavedPrayer(
                    uuid = PrayerId("4"),
                    text = "4",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant.plus(1.milliseconds),
                    createdAt = initialInstant,
                    updatedAt = initialInstant.plus(1.milliseconds),
                ),
                SavedPrayer(
                    uuid = PrayerId("5"),
                    text = "5",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
                SavedPrayer(
                    uuid = PrayerId("6"),
                    text = "6",
                    prayerType = SavedPrayerType.ScheduledCompletable(
                        initialInstant.plus(10.milliseconds).toLocalDateTime(timeZone),
                    ),
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
            )

            // the next time we run it, automatically archive the scheduled prayer past its time
            clock.advanceTimeBy(20.milliseconds)
            useCase()
            repository.getPrayers(ArchiveStatus.FullCollection, emptySet(), emptySet()).first() shouldBe listOf(
                getPrayer(0L, "1", false),
                getPrayer(0L, "2", false),
                getPrayer(0L, "3", true),
                SavedPrayer(
                    uuid = PrayerId("4"),
                    text = "4",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant.plus(1.milliseconds),
                    createdAt = initialInstant,
                    updatedAt = initialInstant.plus(1.milliseconds),
                ),
                SavedPrayer(
                    uuid = PrayerId("5"),
                    text = "5",
                    prayerType = SavedPrayerType.ScheduledCompletable(initialInstant.toLocalDateTime(timeZone)),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant,
                    createdAt = initialInstant,
                    updatedAt = initialInstant,
                ),
                SavedPrayer(
                    uuid = PrayerId("6"),
                    text = "6",
                    prayerType = SavedPrayerType.ScheduledCompletable(
                        initialInstant.plus(10.milliseconds).toLocalDateTime(timeZone),
                    ),
                    tags = emptyList(),
                    archived = true,
                    archivedAt = initialInstant.plus(21.milliseconds),
                    createdAt = initialInstant,
                    updatedAt = initialInstant.plus(21.milliseconds),
                ),
            )
        }
    }
})
