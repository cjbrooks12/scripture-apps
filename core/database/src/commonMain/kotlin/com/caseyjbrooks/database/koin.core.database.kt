package com.caseyjbrooks.database

import app.cash.sqldelight.db.SqlDriver
import com.caseyjbrooks.database.adapters.InstantAdapter
import com.caseyjbrooks.database.adapters.LocalDateAdapter
import com.caseyjbrooks.database.adapters.UuidAdapter
import org.koin.core.module.Module
import org.koin.dsl.module

private fun createDatabase(driver: SqlDriver): ScriptureNowDatabase {
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

internal expect fun getRealPlatformDatabaseModule(): Module
public val realDatabaseModule: Module = module {
    includes(getRealPlatformDatabaseModule())
    single<ScriptureNowDatabase> {
        createDatabase(get())
    }
    single<UuidFactory> { RealUuidFactory() }
}

internal expect fun getFakePlatformDatabaseModule(): Module
public val fakeDatabaseModule: Module = module {
    includes(getFakePlatformDatabaseModule())
    single<ScriptureNowDatabase> {
        createDatabase(get())
    }
    single<UuidFactory> { FakeUuidFactory() }
}
