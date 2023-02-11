package com.caseyjbrooks.scripturenow.api.votd.ourmanna

import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApiProvider
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.OurMannaApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models.OurMannaVotdResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.now
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

public class TestOurMannaApi : StringSpec({
    "test responseConverter" {
        val inputJson = """
            {
                "verse": {
                    "details": {
                        "text": "Whoever has the Son has life; whoever does not have the Son of God does not have life.",
                        "reference": "1 John 5:12",
                        "version": "NIV",
                        "verseurl": "http://www.ourmanna.com/"
                    },
                    "notice": "Powered by OurManna.com"
                }
            }
        """.trimIndent()
        val inputParsed = HttpClientProvider.json.decodeFromString(OurMannaVotdResponse.serializer(), inputJson)
        val now = LocalDate.now()
        val converted = OurMannaApiConverterImpl().convertApiModelToRepositoryModel(
            date = now,
            apiModel = inputParsed,
        )

        converted.date shouldBe now
        converted.providedBy shouldBe VerseOfTheDayService.OurManna
        converted.text shouldBe "Whoever has the Son has life; whoever does not have the Son of God does not have life."
        converted.reference shouldBe VerseReference.KnownReference("1 John", 5, 12..12)
        converted.version shouldBe "NIV"
        converted.verseUrl shouldBe "http://www.ourmanna.com/"
        converted.notice shouldBe "Powered by OurManna.com"
    }
    "test making API call" {
        // this should throw and exception if something goes wrong. If it doesn't throw, the test passes
        VerseOfTheDayApiProvider
            .get(VerseOfTheDayService.OurManna)
            .getTodaysVerseOfTheDay().providedBy shouldBe VerseOfTheDayService.OurManna
    }
})
