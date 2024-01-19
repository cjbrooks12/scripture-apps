package com.caseyjbrooks.database

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val databaseModule: Module = module {
    singleOf(ScriptureNowDatabase::invoke)
}
