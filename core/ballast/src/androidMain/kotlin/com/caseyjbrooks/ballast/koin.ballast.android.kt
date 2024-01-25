package com.caseyjbrooks.ballast

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformBallastModule(): Module = module {}
internal actual fun getFakePlatformBallastModule(): Module = module {}
