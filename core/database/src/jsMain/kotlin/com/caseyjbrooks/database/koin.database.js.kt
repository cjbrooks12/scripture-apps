package com.caseyjbrooks.database

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformDatabaseModule(): Module = module {}
internal actual fun getFakePlatformDatabaseModule(): Module = module {}
