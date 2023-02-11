package com.caseyjbrooks.scripturenow.db.votd

import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.db.votd.sql.VerseOfTheDaySqlDb
import com.caseyjbrooks.scripturenow.db.votd.sql.VerseOfTheDaySqlDbConverterImpl

public object VerseOfTheDayDbProvider {
    public fun get(
        database: ScriptureNowDatabase,
    ): VerseOfTheDayDb {
        return VerseOfTheDaySqlDb(
            queries = database.sn_verseOfTheDayQueries,
            converter = VerseOfTheDaySqlDbConverterImpl()
        )
    }
}
