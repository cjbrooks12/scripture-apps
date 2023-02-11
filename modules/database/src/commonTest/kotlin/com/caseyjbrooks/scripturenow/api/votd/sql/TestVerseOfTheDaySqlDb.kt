package com.caseyjbrooks.scripturenow.api.votd.sql

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.db.impl.sql.SqlDbProvider
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDbProvider
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.now
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

class TestVerseOfTheDaySqlDb : StringSpec({
    "test making DB calls" {
        val verseOfTheDayDatabase = VerseOfTheDayDbProvider.get(
            SqlDbProvider.getDatabase(
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
                    ScriptureNowDatabase.Schema.create(it)
                }
            )
        )

        val now = LocalDate.now()

        verseOfTheDayDatabase.saveVerseOfTheDay(
            VerseOfTheDay(
                uuid = uuid4(),
                providedBy = VerseOfTheDayService.OurManna,
                date = LocalDate.now(),
                text = "Whoever has the Son has life; whoever does not have the Son of God does not have life.",
                reference = VerseReference.KnownReference("1 John", 5, 12..12),
                version = "NIV",
                verseUrl = "http://www.ourmanna.com/",
                notice = "Powered by OurManna.com",
            )
        )

        val converted = verseOfTheDayDatabase
            .getCachedVerseOfTheDay(VerseOfTheDayService.OurManna, now)
            .first()

        converted shouldNotBe null
        converted!!.date shouldBe now
        converted.providedBy shouldBe VerseOfTheDayService.OurManna
        converted.text shouldBe "Whoever has the Son has life; whoever does not have the Son of God does not have life."
        converted.reference shouldBe VerseReference.KnownReference("1 John", 5, 12..12)
        converted.version shouldBe "NIV"
        converted.verseUrl shouldBe "http://www.ourmanna.com/"
        converted.notice shouldBe "Powered by OurManna.com"

        verseOfTheDayDatabase
            .getCachedVerseOfTheDay(VerseOfTheDayService.VerseOfTheDayDotCom, now)
            .first().shouldBeNull()
    }
})
