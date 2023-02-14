package com.caseyjbrooks.scripturenow.db.prayer

import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.db.prayer.sql.PrayerSqlDb
import com.caseyjbrooks.scripturenow.db.prayer.sql.PrayerSqlDbConverterImpl

public object PrayerDbProvider {
    public fun get(
        database: ScriptureNowDatabase,
    ): PrayerDb {
        return PrayerSqlDb(
            queries = database.sn_prayerQueries,
            converter = PrayerSqlDbConverterImpl(),
        )
    }
}
