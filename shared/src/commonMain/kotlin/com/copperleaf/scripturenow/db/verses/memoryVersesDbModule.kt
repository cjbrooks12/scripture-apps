package com.copperleaf.scripturenow.db.verses

import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.db.verses.sql.MemoryVersesSqlDb
import com.copperleaf.scripturenow.db.verses.sql.MemoryVersesSqlDbConverterImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun memoryVersesDbModule() = DI.Module(name = "DB > Memory Verses") {
    bind<MemoryVersesDb> {
        singleton {
            MemoryVersesSqlDb(
                queries = instance<ScriptureNowDatabase>().sn_memoryVerseQueries,
                converter = MemoryVersesSqlDbConverterImpl(),
            )
        }
    }
}
