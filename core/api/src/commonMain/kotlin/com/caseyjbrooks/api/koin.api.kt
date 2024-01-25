package com.caseyjbrooks.api

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun getRealPlatformApiModule(): Module
public val realApiModule: Module = module {
    includes(getRealPlatformApiModule())
}


internal expect fun getFakePlatformApiModule(): Module
public val fakeApiModule: Module = module {
    includes(getFakePlatformApiModule())
}
