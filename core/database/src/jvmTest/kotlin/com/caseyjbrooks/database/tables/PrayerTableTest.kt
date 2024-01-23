package com.caseyjbrooks.database.tables

import com.caseyjbrooks.database.Prayer
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class PrayerTableTest : StringSpec({
    "Test Prayer table" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()
            val now = Instant.fromEpochMilliseconds(0L)

            val prayer1 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text  = "prayer 1",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer2 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text  = "prayer 2",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer3 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text  = "prayer 3",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer4 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text  = "prayer 4",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )

            with(database.prayersQueries) {
                // check initial state
                getAll().executeAsList() shouldBe emptyList()

                // create prayers
                createPrayer(prayer1)
                createPrayer(prayer2)
                createPrayer(prayer3)
                createPrayer(prayer4)

                // check inserted properly
                getAll().executeAsList() shouldBe listOf(
                    prayer1,
                    prayer2,
                    prayer3,
                    prayer4,
                )

                // delete a prayer
                deletePrayer(prayer2.uuid)

                // check deleted properly
                getAll().executeAsList() shouldBe listOf(
                    prayer1,
                    prayer3,
                    prayer4,
                )

                // delete everything
                deleteAll()

                // check everything deleted properly
                getAll().executeAsList() shouldBe emptyList()
            }
        }
    }
})
