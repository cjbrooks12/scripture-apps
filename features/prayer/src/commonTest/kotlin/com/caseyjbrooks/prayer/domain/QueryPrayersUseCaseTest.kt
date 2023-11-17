package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCase
import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCaseImpl
import com.caseyjbrooks.prayer.utils.getPrayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

public class QueryPrayersUseCaseTest : StringSpec({
    suspend fun getRepository(): SavedPrayersRepository {
        val repository = InMemorySavedPrayersRepository()

        repository.createPrayer(getPrayer("1", false, "one"))
        repository.createPrayer(getPrayer("2", false, "one"))
        repository.createPrayer(getPrayer("3", false, "one"))

        repository.createPrayer(getPrayer("4", false, "two"))
        repository.createPrayer(getPrayer("5", false, "two"))
        repository.createPrayer(getPrayer("6", false, "two"))

        repository.createPrayer(getPrayer("7", false, "one", "two"))
        repository.createPrayer(getPrayer("8", false, "one", "two"))

        repository.createPrayer(getPrayer("9", true, "one"))
        repository.createPrayer(getPrayer("10", true, "two"))
        repository.createPrayer(getPrayer("11", true, "one", "two"))

        return repository
    }

    "query prayers > not archived, no tags" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.NotArchived,
            tags = emptyList(),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("1", false, "one"),
            getPrayer("2", false, "one"),
            getPrayer("3", false, "one"),
            getPrayer("4", false, "two"),
            getPrayer("5", false, "two"),
            getPrayer("6", false, "two"),
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
        )
    }

    "query prayers > not archived, tag 'one'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.NotArchived,
            tags = listOf(PrayerTag("one")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("1", false, "one"),
            getPrayer("2", false, "one"),
            getPrayer("3", false, "one"),
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
        )
    }

    "query prayers > not archived, tag 'one' and 'two'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.NotArchived,
            tags = listOf(PrayerTag("one"), PrayerTag("two")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
        )
    }

    "query prayers > archived, no tags" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.Archived,
            tags = emptyList(),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("9", true, "one"),
            getPrayer("10", true, "two"),
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > archived, tag 'one'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.Archived,
            tags = listOf(PrayerTag("one")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("9", true, "one"),
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > archived, tags 'one' and 'two'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.Archived,
            tags = listOf(PrayerTag("one"), PrayerTag("two")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > full collection, no tags" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.FullCollection,
            tags = emptyList(),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("1", false, "one"),
            getPrayer("2", false, "one"),
            getPrayer("3", false, "one"),
            getPrayer("4", false, "two"),
            getPrayer("5", false, "two"),
            getPrayer("6", false, "two"),
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
            getPrayer("9", true, "one"),
            getPrayer("10", true, "two"),
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > full collection, tag 'one'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.FullCollection,
            tags = listOf(PrayerTag("one")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("1", false, "one"),
            getPrayer("2", false, "one"),
            getPrayer("3", false, "one"),
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
            getPrayer("9", true, "one"),
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > full collection, tag 'one' and 'two'" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.FullCollection,
            tags = listOf(PrayerTag("one"), PrayerTag("two")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe listOf(
            getPrayer("7", false, "one", "two"),
            getPrayer("8", false, "one", "two"),
            getPrayer("11", true, "one", "two"),
        )
    }

    "query prayers > no matches" {
        val useCase: QueryPrayersUseCase = QueryPrayersUseCaseImpl(getRepository())

        val results = useCase(
            archiveStatus = ArchiveStatus.FullCollection,
            tags = listOf(PrayerTag("three")),
        ).take(2).toList()

        results[0].shouldBeInstanceOf<Cached.Fetching<List<SavedPrayer>>>()
        results[0].getCachedOrNull() shouldBe null

        results[1].shouldBeInstanceOf<Cached.Value<SavedPrayer>>()
        results[1].getCachedOrNull() shouldBe emptyList()
    }
})
