package com.caseyjbrooks.scripturenow.db.memory

import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.db.memory.sql.MemoryVerseSqlDb
import com.caseyjbrooks.scripturenow.db.memory.sql.MemoryVerseSqlDbConverterImpl

public object MemoryVerseDbProvider {
    public fun get(
        database: ScriptureNowDatabase,
    ): MemoryVerseDb {
        return MemoryVerseSqlDb(
            queries = database.sn_memoryVerseQueries,
            converter = MemoryVerseSqlDbConverterImpl()
        )
    }
}
