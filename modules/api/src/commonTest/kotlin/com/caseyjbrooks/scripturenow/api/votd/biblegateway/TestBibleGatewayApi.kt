package com.caseyjbrooks.scripturenow.api.votd.biblegateway

import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApiProvider
import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.BibleGatewayApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models.BibleGatewayVotdResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.now
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

public class TestBibleGatewayApi : StringSpec({
    "test responseConverter" {
        val inputJson = """
            <?xml version="1.0" ?>
            <rss version="2.0"
                 xmlns:dc="http://purl.org/dc/elements/1.1/"
                 xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
                 xmlns:admin="http://webns.net/mvcb/"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:content="http://purl.org/rss/1.0/modules/content/">
            
                <channel>
                    <title>Bible Gateway's Verse of the Day</title>
                    <link>https://www.biblegateway.com</link>
                    <description>A daily word of exultation.</description>
                    <dc:language>en-us</dc:language>
                    <dc:creator>BibleGateway.com</dc:creator>
                    <dc:rights>Copyright 2004</dc:rights>
                    <dc:date>2022-10-31T12:00:00Z</dc:date>
                    <sy:updatePeriod>daily</sy:updatePeriod>
                    <sy:updateFrequency>1</sy:updateFrequency>
                    <sy:updateBase>2000-01-01T12:00+00:00</sy:updateBase>
            
                    <item>
                        <title>1 Peter 5:8-9</title>
                        <description>Verse of the day</description>
                        <guid isPermaLink="false">https://www.biblegateway.com/passage/?search=1+Peter+5%3A8-9&amp;version=31</guid>
                        <content:encoded>
                            <![CDATA[&ldquo;Be alert and of sober mind. Your enemy the devil prowls around like a roaring lion looking for someone to devour.   Resist him, standing firm in the faith, because you know that the family of believers throughout the world is undergoing the same kind of sufferings.&rdquo;<br/><br/> Brought to you by <a href="https://www.biblegateway.com">BibleGateway.com</a>. Copyright (C) NIV. All Rights Reserved.]]>
                        </content:encoded>
                        <dc:rights>Powered by BibleGateway.com</dc:rights>
                        <dc:date>2022-10-31T12:00:00Z</dc:date>
                    </item>
                </channel>
            </rss>
        """.trimIndent()
        val inputParsed = HttpClientProvider.xml.decodeFromString(BibleGatewayVotdResponse.serializer(), inputJson)
        val now = LocalDate.now()
        val converted = BibleGatewayApiConverterImpl().convertApiModelToRepositoryModel(
            date = now,
            apiModel = inputParsed,
        )

        converted.date shouldBe now
        converted.text shouldBe "&ldquo;Be alert and of sober mind. Your enemy the devil prowls around like a roaring lion looking for someone to devour.   Resist him, standing firm in the faith, because you know that the family of believers throughout the world is undergoing the same kind of sufferings.&rdquo;"
        converted.reference shouldBe VerseReference.KnownReference("1 Peter", 5, 8..9)
        converted.version shouldBe "NIV"
        converted.verseUrl shouldBe "https://www.biblegateway.com/passage/?search=1+Peter+5%3A8-9&version=31"
        converted.notice shouldBe "Powered by BibleGateway.com"
    }
    "test making API call" {
        // this should throw and exception if something goes wrong. If it doesn't throw, the test passes
        VerseOfTheDayApiProvider
            .get(VerseOfTheDayService.BibleGateway)
            .getTodaysVerseOfTheDay().providedBy shouldBe VerseOfTheDayService.BibleGateway
    }
})
