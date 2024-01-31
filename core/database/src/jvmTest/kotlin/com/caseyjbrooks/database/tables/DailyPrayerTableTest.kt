package com.caseyjbrooks.database.tables

import com.caseyjbrooks.database.Daily_prayer
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

class DailyPrayerTableTest : StringSpec({
    "Test Daily Prayer table" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()

            val prayer1 = Daily_prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "Daily prayer for Jan 1, 2024",
                date = LocalDate(2024, Month.JANUARY, 1),
            )
            val prayer2 = Daily_prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "Daily prayer for Jan 2, 2024",
                date = LocalDate(2024, Month.JANUARY, 2),
            )
            val prayer3 = Daily_prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "Daily prayer for Jan 3, 2024",
                date = LocalDate(2024, Month.JANUARY, 3),
            )
            val prayer4 = Daily_prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "Daily prayer for Jan 4, 2024",
                date = LocalDate(2024, Month.JANUARY, 4),
            )

            with(database.dailyPrayerQueries) {
                // initial state
                getAll().executeAsList() shouldBe emptyList()
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull().shouldBeNull()

                // add a daily prayer
                insertOrReplace(prayer1)
                insertOrReplace(prayer2)
                insertOrReplace(prayer3)
                insertOrReplace(prayer4)

                // check the daily prayer was saved
                getAll().executeAsList() shouldBe listOf(
                    prayer1,
                    prayer2,
                    prayer3,
                    prayer4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull() shouldBe prayer1
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe prayer2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull() shouldBe prayer3
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe prayer4

                // remove a daily prayer by ID
                deleteById(prayer1.uuid)

                // check the daily prayer was removed
                getAll().executeAsList() shouldBe listOf(
                    prayer2,
                    prayer3,
                    prayer4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe prayer2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull() shouldBe prayer3
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe prayer4

                // remove a daily prayer by date
                deleteByDate(prayer3.date)

                // check the daily prayer was removed
                getAll().executeAsList() shouldBe listOf(
                    prayer2,
                    prayer4,
                )
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull() shouldBe prayer2
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull() shouldBe prayer4

                // remove a daily prayer by date
                deleteAll()

                // check all prayers deleted
                getAll().executeAsList() shouldBe emptyList()
                getByDay(LocalDate(2024, 1, 1)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 2)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 3)).executeAsOneOrNull().shouldBeNull()
                getByDay(LocalDate(2024, 1, 4)).executeAsOneOrNull().shouldBeNull()
            }
        }
    }
})
