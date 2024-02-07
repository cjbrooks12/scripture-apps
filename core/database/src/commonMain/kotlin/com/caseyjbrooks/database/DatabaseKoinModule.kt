package com.caseyjbrooks.database

import com.caseyjbrooks.database.adapters.createDatabase
import com.caseyjbrooks.di.KoinModule

import org.koin.core.module.Module
import org.koin.dsl.module

public class DatabaseKoinModule : KoinModule {

    override fun platformKoinModule(): KoinModule? = DatabaseKoinPlatformModule()

    override fun mainModule(): Module = module {
        single<ScriptureNowDatabase> {
            createDatabase(get())
        }
    }

    override fun testModule(): Module = module {
        single<UuidFactory> { FakeUuidFactory() }
    }

    override fun localModule(): Module {
        return productionModule()
    }

    override fun qaModule(): Module {
        return productionModule()
    }

    override fun productionModule(): Module = module {
        single<UuidFactory> { RealUuidFactory() }
    }
}
