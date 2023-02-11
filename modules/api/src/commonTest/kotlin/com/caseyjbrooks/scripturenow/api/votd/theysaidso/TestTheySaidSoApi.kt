package com.caseyjbrooks.scripturenow.api.votd.theysaidso

import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.TheySaidSoApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.utils.now
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

public class TestTheySaidSoApi : StringSpec({
    "test responseConverter" {
        val inputJson = """
            {
                "success": {
                    "total": 1
                },
                "contents": {
                    "verse": "Neither do men light a candle, and put it under a bushel, but on a candlestick; and it gives light to all that are in the house.",
                    "number": "15",
                    "chapter": "5",
                    "book": "Matthew",
                    "testament": "New Testament",
                    "bookid": "40",
                    "uuid": "9abu0MV11FsgcFWLQZoXsAeF"
                }
            }
        """.trimIndent()
        val inputParsed = HttpClientProvider.json.decodeFromString(TheySaidSoBibleVerseResponse.serializer(), inputJson)
        val now = LocalDate.now()
        val converted = TheySaidSoApiConverterImpl().convertApiModelToRepositoryModel(
            date = now,
            apiModel = inputParsed,
        )

        converted.date shouldBe now
        converted.text shouldBe "Neither do men light a candle, and put it under a bushel, but on a candlestick; and it gives light to all that are in the house."
        converted.reference shouldBe VerseReference.KnownReference("Matthew", 5, 15..15)
        converted.version shouldBe ""
        converted.verseUrl shouldBe "https://theysaidso.com"
        converted.notice shouldBe "Powered by quotes from theysaidso.com"
    }
    "test making API call" {
//        // this should throw and exception if something goes wrong. If it doesn't throw, the test passes
//        VerseOfTheDayApiProvider
//            .get(VerseOfTheDayService.TheySaidSo)
//            .getTodaysVerseOfTheDay().providedBy shouldBe VerseOfTheDayService.TheySaidSo
    }
})
