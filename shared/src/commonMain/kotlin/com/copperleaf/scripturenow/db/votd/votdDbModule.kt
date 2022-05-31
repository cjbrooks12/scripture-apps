package com.copperleaf.scripturenow.db.votd

import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.db.votd.sql.VerseOfTheDaySqlDb
import com.copperleaf.scripturenow.db.votd.sql.VerseOfTheDaySqlDbConverterImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun votdDbModule() = DI.Module(name = "DB > VOTD") {
    bind<VerseOfTheDayDb> {
        singleton {
            VerseOfTheDaySqlDb(
                queries = instance<ScriptureNowDatabase>().sn_verseOfTheDayQueries,
                converter = VerseOfTheDaySqlDbConverterImpl(),
            )
        }
    }
}
