package com.caseyjbrooks.database.tables

import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.Verse_of_the_day
import com.caseyjbrooks.database.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

class VerseOfTheDayTableTest : StringSpec({
    "Test Verse Of The Day table" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()

            val verse1 = Verse_of_the_day(
                uuid = uuidFactory.getNewUuid(),
                reference = "Genesis 1:1",
                text = "Verse of the day for Jan 1, 2024",
                date = LocalDate(2024, Month.JANUARY, 1),
            )
            val verse2 = Verse_of_the_day(
                uuid = uuidFactory.getNewUuid(),
                reference = "Genesis 1:2",
                text = "Verse of the day for Jan 2, 2024",
                date = LocalDate(2024, Month.JANUARY, 2),
            )
            val verse3 = Verse_of_the_day(
                uuid = uuidFactory.getNewUuid(),
                reference = "Genesis 1:3",
                text = "Verse of the day for Jan 3, 2024",
                date = LocalDate(2024, Month.JANUARY, 3),
            )
            val verse4 = Verse_of_the_day(
                uuid = uuidFactory.getNewUuid(),
                reference = "Genesis 1:3",
                text = "Verse of the day for Jan 4, 2024",
                date = LocalDate(2024, Month.JANUARY, 4),
            )

            with(database.verse_of_the_dayQueries) {
                // initial state
                getAll().executeAsList() shouldBe emptyList()
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull().shouldBeNull()

                // add a Verse of the Day
                insertOrReplace(verse1)
                insertOrReplace(verse2)
                insertOrReplace(verse3)
                insertOrReplace(verse4)

                // check the Verse of the Day was saved
                getAll().executeAsList() shouldBe listOf(
                    verse1,
                    verse2,
                    verse3,
                    verse4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull() shouldBe verse1
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe verse2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull() shouldBe verse3
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe verse4

                // remove a Verse of the Day by ID
                deleteById(verse1.uuid)

                // check the Verse of the Day was removed
                getAll().executeAsList() shouldBe listOf(
                    verse2,
                    verse3,
                    verse4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe verse2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull() shouldBe verse3
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe verse4

                // remove a Verse of the Day by date
                deleteByDate(verse3.date)

                // check the Verse of the Day was removed
                getAll().executeAsList() shouldBe listOf(
                    verse2,
                    verse4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe verse2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe verse4

                // remove a Verse of the Day by date
                deleteAll()

                // check all verses deleted
                getAll().executeAsList() shouldBe emptyList()
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull().shouldBeNull()
            }
        }
    }
})
