package com.caseyjbrooks.database

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val realDatabaseModule: Module = module {
    singleOf(ScriptureNowDatabase::invoke)
    single<UuidFactory> { RealUuidFactory() }
}

public val fakeDatabaseModule: Module = module {
    singleOf(ScriptureNowDatabase::invoke)
    single<UuidFactory> { FakeUuidFactory("1") }
}
