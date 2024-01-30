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
        prayerAdapter = Prayer.Adapter(
            uuidAdapter = UuidAdapter,
            autoArchiveAtAdapter = InstantAdapter,
            archivedAtAdapter = InstantAdapter,
            createdAtAdapter = InstantAdapter,
            updatedAtAdapter = InstantAdapter,
        ),
        prayer_tagAdapter = Prayer_tag.Adapter(
            prayer_uuidAdapter = UuidAdapter,
            tag_uuidAdapter = UuidAdapter,
        ),
        tagAdapter = Tag.Adapter(
            uuidAdapter = UuidAdapter,
        ),
        verse_of_the_dayAdapter = Verse_of_the_day.Adapter(
            uuidAdapter = UuidAdapter,
            dateAdapter = LocalDateAdapter,
        ),
        daily_prayerAdapter = Daily_prayer.Adapter(
            uuidAdapter = UuidAdapter,
            dateAdapter = LocalDateAdapter,
        )
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
