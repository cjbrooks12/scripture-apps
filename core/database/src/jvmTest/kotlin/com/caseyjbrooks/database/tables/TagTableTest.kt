package com.caseyjbrooks.database.tables

import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.Tag
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TagTableTest : StringSpec({
    "Basic create and delete tags" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()

            val tag1 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 1"
            )
            val tag2 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 2"
            )
            val tag3 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 3"
            )
            val tag4 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 4"
            )

            with(database.tagQueries) {
                // check initial state
                getAll().executeAsList() shouldBe emptyList()

                // insert tags
                createTag(tag1)
                createTag(tag2)
                createTag(tag3)
                createTag(tag4)

                // check tags inserted properly
                getAll().executeAsList() shouldBe listOf(
                    tag1,
                    tag2,
                    tag3,
                    tag4,
                )

                // delete a tag by ID
                deleteById(tag1.uuid)

                // check tags deleted properly
                getAll().executeAsList() shouldBe listOf(
                    tag2,
                    tag3,
                    tag4,
                )

                // delete by name
                deleteByName(tag3.name)

                // check tags deleted properly
                getAll().executeAsList() shouldBe listOf(
                    tag2,
                    tag4,
                )

                // delete all
                deleteAll()

                // check all tags deleted
                getAll().executeAsList() shouldBe emptyList()
            }
        }
    }

    "Reinserting tag with same name is ignored" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()

            val tag1 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 1"
            )
            val tag2 = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 1"
            )

            with(database.tagQueries) {
                getAll().executeAsList() shouldBe emptyList()

                createTag(tag1)
                createTag(tag2)

                getAll().executeAsList() shouldBe listOf(
                    tag1
                )
            }
        }
    }

    "Update tag name" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()

            val tag = Tag(
                uuid = uuidFactory.getNewUuid(),
                name = "tag 1",
            )

            with(database.tagQueries) {
                createTag(tag)
                getAll().executeAsList() shouldBe listOf(
                    tag
                )

                updateTag(
                    name = "updated tag",
                    uuid = tag.uuid,
                )

                getAll().executeAsList() shouldBe listOf(
                    Tag(
                        uuid = tag.uuid,
                        name = "updated tag",
                    )
                )
            }
        }
    }
})
