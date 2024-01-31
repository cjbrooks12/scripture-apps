package com.caseyjbrooks.database.tables

import com.caseyjbrooks.database.Prayer
import com.caseyjbrooks.database.Prayer_tag
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.Tag
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.koinTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class PrayerTagTableTest : StringSpec({
    "Test create and query prayers with tags" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()
            val now = Instant.fromEpochMilliseconds(0L)

            val prayer1 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 1",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer2 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 2",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer3 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 3",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer4 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 4",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )

            // tags
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

            // initialize DB structure
            with(database.prayersQueries) {
                createPrayer(prayer1)
                createPrayer(prayer2)
                createPrayer(prayer3)
                createPrayer(prayer4)
            }
            with(database.tagQueries) {
                createTag(tag1)
                createTag(tag2)
                createTag(tag3)
            }
            with(database.prayerTagQueries) {
                createPrayerTag(Prayer_tag(prayer1.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer2.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer3.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag1.uuid))

                createPrayerTag(Prayer_tag(prayer2.uuid, tag2.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag2.uuid))

                createPrayerTag(Prayer_tag(prayer4.uuid, tag3.uuid))
            }

            // Check basic associations
            with(database.prayerTagQueries) {
                getPrayersForTag(tag1.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer1.uuid,
                    prayer2.uuid,
                    prayer3.uuid,
                    prayer4.uuid,
                )
                getPrayersForTag(tag2.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer2.uuid,
                    prayer4.uuid,
                )
                getPrayersForTag(tag3.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer4.uuid,
                )

                getTagsForPrayer(prayer1.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer2.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                    tag2.uuid,
                )
                getTagsForPrayer(prayer3.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer4.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                    tag2.uuid,
                    tag3.uuid,
                )
            }
        }
    }

    "Remove prayer, check cascade" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()
            val now = Instant.fromEpochMilliseconds(0L)

            val prayer1 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 1",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer2 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 2",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer3 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 3",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer4 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 4",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )

            // tags
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

            // initialize DB structure
            with(database.prayersQueries) {
                createPrayer(prayer1)
                createPrayer(prayer2)
                createPrayer(prayer3)
                createPrayer(prayer4)
            }
            with(database.tagQueries) {
                createTag(tag1)
                createTag(tag2)
                createTag(tag3)
            }
            with(database.prayerTagQueries) {
                createPrayerTag(Prayer_tag(prayer1.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer2.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer3.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag1.uuid))

                createPrayerTag(Prayer_tag(prayer2.uuid, tag2.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag2.uuid))

                createPrayerTag(Prayer_tag(prayer4.uuid, tag3.uuid))
            }

            // remove a prayer, check the cascade
            database.prayersQueries.deletePrayer(prayer4.uuid)
            with(database.prayerTagQueries) {
                getPrayersForTag(tag1.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer1.uuid,
                    prayer2.uuid,
                    prayer3.uuid,
                )
                getPrayersForTag(tag2.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer2.uuid,
                )
                getPrayersForTag(tag3.uuid).executeAsList().map { it.prayer_uuid } shouldBe emptyList()

                getTagsForPrayer(prayer1.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer2.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                    tag2.uuid,
                )
                getTagsForPrayer(prayer3.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer4.uuid).executeAsList().map { it.tag_uuid } shouldBe emptyList()
            }
        }
    }

    "Remove tag, check cascade" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()
            val now = Instant.fromEpochMilliseconds(0L)

            val prayer1 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 1",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer2 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 2",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer3 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 3",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer4 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 4",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )

            // tags
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

            // initialize DB structure
            with(database.prayersQueries) {
                createPrayer(prayer1)
                createPrayer(prayer2)
                createPrayer(prayer3)
                createPrayer(prayer4)
            }
            with(database.tagQueries) {
                createTag(tag1)
                createTag(tag2)
                createTag(tag3)
            }
            with(database.prayerTagQueries) {
                createPrayerTag(Prayer_tag(prayer1.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer2.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer3.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag1.uuid))

                createPrayerTag(Prayer_tag(prayer2.uuid, tag2.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag2.uuid))

                createPrayerTag(Prayer_tag(prayer4.uuid, tag3.uuid))
            }

            // remove a tag, check the cascade
            database.tagQueries.deleteById(tag1.uuid)
            with(database.prayerTagQueries) {
                getPrayersForTag(tag1.uuid).executeAsList().map { it.prayer_uuid } shouldBe emptyList()
                getPrayersForTag(tag2.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer2.uuid,
                    prayer4.uuid,
                )
                getPrayersForTag(tag3.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer4.uuid,
                )

                getTagsForPrayer(prayer1.uuid).executeAsList().map { it.tag_uuid } shouldBe emptyList()
                getTagsForPrayer(prayer2.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag2.uuid,
                )
                getTagsForPrayer(prayer3.uuid).executeAsList().map { it.tag_uuid } shouldBe emptyList()
                getTagsForPrayer(prayer4.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag2.uuid,
                    tag3.uuid,
                )
            }
        }
    }

    "Remove tag from prayer" {
        koinTest {
            val database: ScriptureNowDatabase = get()
            val uuidFactory: UuidFactory = get()
            val now = Instant.fromEpochMilliseconds(0L)

            val prayer1 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 1",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer2 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 2",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer3 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 3",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )
            val prayer4 = Prayer(
                uuid = uuidFactory.getNewUuid(),
                text = "prayer 4",
                autoArchiveAt = null,
                archivedAt = null,
                createdAt = now,
                updatedAt = now,
            )

            // tags
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

            // initialize DB structure
            with(database.prayersQueries) {
                createPrayer(prayer1)
                createPrayer(prayer2)
                createPrayer(prayer3)
                createPrayer(prayer4)
            }
            with(database.tagQueries) {
                createTag(tag1)
                createTag(tag2)
                createTag(tag3)
            }
            with(database.prayerTagQueries) {
                createPrayerTag(Prayer_tag(prayer1.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer2.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer3.uuid, tag1.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag1.uuid))

                createPrayerTag(Prayer_tag(prayer2.uuid, tag2.uuid))
                createPrayerTag(Prayer_tag(prayer4.uuid, tag2.uuid))

                createPrayerTag(Prayer_tag(prayer4.uuid, tag3.uuid))
            }

            // remove the tag from a prayer
            database.prayerTagQueries.removeTagFromPrayer(prayer2.uuid, tag2.uuid)
            with(database.prayerTagQueries) {
                getPrayersForTag(tag1.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer1.uuid,
                    prayer2.uuid,
                    prayer3.uuid,
                    prayer4.uuid,
                )
                getPrayersForTag(tag2.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer4.uuid,
                )
                getPrayersForTag(tag3.uuid).executeAsList().map { it.prayer_uuid } shouldBe listOf(
                    prayer4.uuid,
                )

                getTagsForPrayer(prayer1.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer2.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer3.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                )
                getTagsForPrayer(prayer4.uuid).executeAsList().map { it.tag_uuid } shouldBe listOf(
                    tag1.uuid,
                    tag2.uuid,
                    tag3.uuid,
                )
            }
        }
    }
})
