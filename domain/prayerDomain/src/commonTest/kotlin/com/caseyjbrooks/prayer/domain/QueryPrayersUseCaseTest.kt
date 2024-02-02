package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.utils.PrayerId
import com.caseyjbrooks.prayer.utils.koinTest
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

public class QueryPrayersUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: Int, archived: Boolean, vararg tags: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = id.toString(),
            prayerType = SavedPrayerType.Persistent,
            tags = tags.map { PrayerTag(it) },
            archived = archived,
            archivedAt = if (archived) instant else null,
            notification = PrayerNotification.None,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    fun getScheduledPrayer(millis: Long, id: Int, archived: Boolean, vararg tags: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = id.toString(),
            prayerType = SavedPrayerType.ScheduledCompletable(
                LocalDateTime(2024, Month.JANUARY, 1, 1, 1, 1, 1)
                    .toInstant(TimeZone.UTC)
            ),
            tags = tags.map { PrayerTag(it) },
            archived = archived,
            archivedAt = if (archived) instant else null,
            notification = PrayerNotification.None,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    suspend fun SavedPrayersRepository.populate() {
        createPrayer(getPrayer(0L, 1, false, "one"))
        createPrayer(getPrayer(0L, 2, false, "one"))
        createPrayer(getPrayer(0L, 3, false, "one"))

        createPrayer(getPrayer(0L, 4, false, "two"))
        createPrayer(getPrayer(0L, 5, false, "two"))
        createPrayer(getPrayer(0L, 6, false, "two"))

        createPrayer(getPrayer(0L, 7, false, "one", "two"))
        createPrayer(getScheduledPrayer(0L, 8, false, "one", "two"))

        createPrayer(getPrayer(0L, 9, true, "one"))
        createPrayer(getPrayer(0L, 10, true, "two"))
        createPrayer(getPrayer(0L, 11, true, "one", "two"))
    }

    "query prayers > not archived, no tags" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()

            val results = useCase(
                archiveStatus = ArchiveStatus.NotArchived,
                prayerType = emptySet(),
                tags = emptySet(),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 1, false, "one"),
                getPrayer(0L, 2, false, "one"),
                getPrayer(0L, 3, false, "one"),
                getPrayer(0L, 4, false, "two"),
                getPrayer(0L, 5, false, "two"),
                getPrayer(0L, 6, false, "two"),
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
            )
        }
    }

    "query prayers > not archived, tag 'one'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()

            val results = useCase(
                archiveStatus = ArchiveStatus.NotArchived,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 1, false, "one"),
                getPrayer(0L, 2, false, "one"),
                getPrayer(0L, 3, false, "one"),
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
            )
        }
    }

    "query prayers > not archived, tag 'one' and 'two'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.NotArchived,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one"), PrayerTag("two")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
            )
        }
    }

    "query prayers > archived, no tags" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.Archived,
                prayerType = emptySet(),
                tags = emptySet(),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 9, true, "one"),
                getPrayer(0L, 10, true, "two"),
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > archived, tag 'one'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.Archived,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 9, true, "one"),
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > archived, tags 'one' and 'two'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.Archived,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one"), PrayerTag("two")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > full collection, no tags" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.FullCollection,
                prayerType = emptySet(),
                tags = emptySet(),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 1, false, "one"),
                getPrayer(0L, 2, false, "one"),
                getPrayer(0L, 3, false, "one"),
                getPrayer(0L, 4, false, "two"),
                getPrayer(0L, 5, false, "two"),
                getPrayer(0L, 6, false, "two"),
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
                getPrayer(0L, 9, true, "one"),
                getPrayer(0L, 10, true, "two"),
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > full collection, tag 'one'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.FullCollection,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 1, false, "one"),
                getPrayer(0L, 2, false, "one"),
                getPrayer(0L, 3, false, "one"),
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
                getPrayer(0L, 9, true, "one"),
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > full collection, tag 'one' and 'two'" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.FullCollection,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("one"), PrayerTag("two")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 7, false, "one", "two"),
                getScheduledPrayer(0L, 8, false, "one", "two"),
                getPrayer(0L, 11, true, "one", "two"),
            )
        }
    }

    "query prayers > no matches" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.FullCollection,
                prayerType = emptySet(),
                tags = setOf(PrayerTag("three")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe emptyList()
        }
    }

    "query prayers > not archived, tag 'one' and 'two', persistent prayers" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.NotArchived,
                prayerType = setOf(SavedPrayerType.Persistent),
                tags = setOf(PrayerTag("one"), PrayerTag("two")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getPrayer(0L, 7, false, "one", "two"),
            )
        }
    }

    "query prayers > not archived, tag 'one' and 'two', scheduled prayers" {
        koinTest {
            val repository: SavedPrayersRepository = get()
            repository.populate()
            val useCase: QueryPrayersUseCase = get()
            val results = useCase(
                archiveStatus = ArchiveStatus.NotArchived,
                prayerType = setOf(
                    SavedPrayerType.ScheduledCompletable(
                        LocalDateTime(2024, Month.JANUARY, 1, 1, 1, 1, 1)
                            .toInstant(TimeZone.UTC)
                    )
                ),
                tags = setOf(PrayerTag("one"), PrayerTag("two")),
            ).take(2).toList()

            results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
            results[0].getCachedOrNull() shouldBe null

            results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
            results[1].getCachedOrNull() shouldBe listOf(
                getScheduledPrayer(0L, 8, false, "one", "two"),
            )
        }
    }
})
