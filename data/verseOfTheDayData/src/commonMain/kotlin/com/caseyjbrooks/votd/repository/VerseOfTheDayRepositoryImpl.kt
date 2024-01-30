package com.caseyjbrooks.votd.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.caseyjbrooks.api.Either
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
) : VerseOfTheDayRepository {

    companion object {
        private const val OURMANNA_HOST = "https://beta.ourmanna.com"
    }

    override suspend fun fetchAndCacheVerseOfTheDay(): Result<Unit> {
        return runCatching<Unit> {
            val todaysDate = clock.now().toLocalDateTime(timeZone).date

            val alreadyFetched = database.verseOftheDayQueries
                .getByDay(todaysDate)
                .executeAsOneOrNull() != null

            if (alreadyFetched) {
                println("VOTD for $todaysDate is already fetched")
                return@runCatching
            }

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

            if (response is Either.Left<OurMannaVerseOfTheDayResponse>) {
                val successBody = response.value

                database.verseOftheDayQueries.insertOrReplace(
                    Verse_of_the_day(
                        uuid = uuidFactory.getNewUuid(),
                        reference = successBody.verse.details.reference,
                        text = successBody.verse.details.text,
                        date = todaysDate,
                    )
                )
            }

            Unit
        }.onFailure { it.printStackTrace() }
    }

    override fun getTodaysVerseOfTheDay(): Flow<VerseOfTheDay?> {
        val todaysDate = clock.now().toLocalDateTime(timeZone).date

        return database.verseOftheDayQueries
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
