package com.caseyjbrooks.votd.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import co.touchlab.kermit.Logger
import com.caseyjbrooks.api.fold
import com.caseyjbrooks.api.request
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.Verse_of_the_day
import com.caseyjbrooks.votd.models.VerseOfTheDay
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonObject

internal class VerseOfTheDayRepositoryImpl(
    private val database: ScriptureNowDatabase,
    private val httpClient: HttpClient,
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val uuidFactory: UuidFactory,
    logger: Logger,
) : VerseOfTheDayRepository {
    private val logger: Logger = logger.withTag("VerseOfTheDayRepositoryImpl")

    companion object {
        private const val OURMANNA_HOST = "https://beta.ourmanna.com"
    }

    override suspend fun fetchAndCacheVerseOfTheDay(): Result<Unit> {
        val todaysDate = clock.now().toLocalDateTime(timeZone).date

        return runCatching<Unit> {
            val alreadyFetched = database.verse_of_the_dayQueries
                .getByDay(todaysDate)
                .executeAsOneOrNull() != null

            if (alreadyFetched) {
                logger.d("VOTD for $todaysDate is already fetched")
                return@runCatching
            }

            logger.d("fetching VOTD for $todaysDate from remote")
            val response = httpClient.request(
                _method = HttpMethod.Get,
                _baseUrl = OURMANNA_HOST,
                _path = "/api/v1/get",
                _queryParameters = mapOf(
                    "format" to "json",
                    "order" to "daily",
                ),
                normalBodySerializer = OurMannaVerseOfTheDayResponse.serializer(),
                errorBodySerializer = JsonObject.serializer(),
            )

            response.fold(
                onLeft = { successBody ->
                    logger.i("fetching VOTD for $todaysDate successful")
                    database.verse_of_the_dayQueries.insertOrReplace(
                        Verse_of_the_day(
                            uuid = uuidFactory.getNewUuid(),
                            reference = successBody.verse.details.reference,
                            text = successBody.verse.details.text,
                            date = todaysDate,
                        )
                    )
                },
                onRight = {
                    logger.e("fetching VOTD for $todaysDate failed")
                },
            )

            Unit
        }.onFailure {
            logger.e("fetching VOTD for $todaysDate failed: ${it.message}")
        }
    }

    override fun getTodaysVerseOfTheDay(): Flow<VerseOfTheDay?> {
        val todaysDate = clock.now().toLocalDateTime(timeZone).date

        return database.verse_of_the_dayQueries
            .getByDay(todaysDate)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.fromRecord() }
    }

// Helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun Verse_of_the_day.fromRecord(): VerseOfTheDay {
        val record = this
        return VerseOfTheDay(
            reference = record.reference,
            verse = record.text,
        )
    }
}
