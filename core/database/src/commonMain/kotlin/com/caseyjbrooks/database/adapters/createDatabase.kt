package com.caseyjbrooks.database.adapters

import app.cash.sqldelight.db.SqlDriver
import com.caseyjbrooks.database.Daily_prayer
import com.caseyjbrooks.database.Prayer
import com.caseyjbrooks.database.Prayer_date
import com.caseyjbrooks.database.Prayer_tag
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.Tag
import com.caseyjbrooks.database.Verse
import com.caseyjbrooks.database.Verse_date
import com.caseyjbrooks.database.Verse_of_the_day
import com.caseyjbrooks.database.Verse_status
import com.caseyjbrooks.database.Verse_tag

internal fun createDatabase(driver: SqlDriver): ScriptureNowDatabase {
    return ScriptureNowDatabase(
        driver = driver,
        daily_prayerAdapter = Daily_prayer.Adapter(
            uuidAdapter = UuidAdapter,
            dateAdapter = LocalDateAdapter,
        ),
        prayerAdapter = Prayer.Adapter(
            uuidAdapter = UuidAdapter,
            autoArchiveAtAdapter = InstantAdapter,
            archivedAtAdapter = InstantAdapter,
            createdAtAdapter = InstantAdapter,
            updatedAtAdapter = InstantAdapter,
        ),
        prayer_dateAdapter = Prayer_date.Adapter(
            prayer_uuidAdapter = UuidAdapter,
            dateAdapter = InstantAdapter,
        ),
        prayer_tagAdapter = Prayer_tag.Adapter(
            prayer_uuidAdapter = UuidAdapter,
            tag_uuidAdapter = UuidAdapter,
        ),
        tagAdapter = Tag.Adapter(
            uuidAdapter = UuidAdapter,
        ),
        verse_dateAdapter = Verse_date.Adapter(
            verse_uuidAdapter = UuidAdapter,
            dateAdapter = InstantAdapter,
        ),
        verse_of_the_dayAdapter = Verse_of_the_day.Adapter(
            uuidAdapter = UuidAdapter,
            dateAdapter = LocalDateAdapter,
        ),
        verse_statusAdapter = Verse_status.Adapter(
            uuidAdapter = UuidAdapter,
            createdAtAdapter = InstantAdapter,
            updatedAtAdapter = InstantAdapter,
        ),
        verse_tagAdapter = Verse_tag.Adapter(
            verse_uuidAdapter = UuidAdapter,
            tag_uuidAdapter = UuidAdapter,
        ),
        verseAdapter = Verse.Adapter(
            uuidAdapter = UuidAdapter,
            statusAdapter = UuidAdapter,
            archivedAtAdapter = InstantAdapter,
            createdAtAdapter = InstantAdapter,
            updatedAtAdapter = InstantAdapter,
        ),
    )
}
