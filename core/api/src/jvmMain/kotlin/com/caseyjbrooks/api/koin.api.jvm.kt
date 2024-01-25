package com.caseyjbrooks.api

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformApiModule(): Module = module {}
internal actual fun getFakePlatformApiModule(): Module = module {}
