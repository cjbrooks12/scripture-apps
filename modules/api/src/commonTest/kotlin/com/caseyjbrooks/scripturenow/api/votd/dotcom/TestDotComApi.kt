package com.caseyjbrooks.scripturenow.api.votd.dotcom

import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApiProvider
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.DotComApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models.DotComVotdResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.now
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

public class TestDotComApi : StringSpec({
    "test responseConverter" {
        val inputHtml = """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
            <html
              xmlns="https://www.w3.org/1999/xhtml"
              xmlns:og="https://ogp.me/ns#"
              xmlns:fb="https://ogp.me/ns/fb#"
              xml:lang="en"
              lang="en"
            >
            <head>
              <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
              <meta http-equiv="content-language" content="en"/>
              <title>Verse of the Day</title>
              <meta name="description" content="A daily devotional featuring a Bible verse, thought, and prayer."/>
              <meta property="og:title" content="Verse of the Day: Ephesians 1:18"/>
              <meta property="og:type" content="article"/>
              <meta property="og:url" content="https://www.verseoftheday.com/en/11012022/"/>
              <meta property="og:image" content="https://www.verseoftheday.com/images/logo_fb_og.png"/>
              <meta
                property="og:description"
                content="I pray also that the eyes of your heart may be enlightened in order that you may know the hope to which he has called you, the riches of his glorious inheritance in the saints."
              />
            </head>
            <body>
            <div id="wrap">
              <div id="featured" class="clear">
                <div class="text-block">
                  <div class="scripture">
                    <div class="bilingual-left">I pray also that the eyes of your heart may be enlightened in order that you may
                      know the hope to which he has called you, the riches of his glorious inheritance in the saints.
                      <div class="reference">—<a href="https://bible.faithlife.com/bible/niv/Ephesians1.18" rel="nofollow">Ephesians
                        1:18</a></div>
                    </div>
                  </div>
                  <div class="end-scripture"></div>
                </div>
              </div>
            </div>
            </body>
            </html>
        """.trimIndent()
        val inputParsed = HttpClientProvider.html.decodeFromString(DotComVotdResponse.serializer(), inputHtml)
        val now = LocalDate.now()
        val converted = DotComApiConverterImpl().convertApiModelToRepositoryModel(
            date = now,
            apiModel = inputParsed,
        )

        converted.date shouldBe now
        converted.text shouldBe "I pray also that the eyes of your heart may be enlightened in order that you may know the hope to which he has called you, the riches of his glorious inheritance in the saints."
        converted.reference shouldBe VerseReference.KnownReference("Ephesians", 1, 18..18)
        converted.version shouldBe "NIV"
        converted.verseUrl shouldBe "https://www.verseoftheday.com/en/11012022/"
        converted.notice shouldBe "Powered by verseoftheday.com"
    }
    "test making API call" {
        // this should throw and exception if something goes wrong. If it doesn't throw, the test passes
        VerseOfTheDayApiProvider
            .get(VerseOfTheDayService.VerseOfTheDayDotCom)
            .getTodaysVerseOfTheDay().providedBy shouldBe VerseOfTheDayService.VerseOfTheDayDotCom
    }
})
