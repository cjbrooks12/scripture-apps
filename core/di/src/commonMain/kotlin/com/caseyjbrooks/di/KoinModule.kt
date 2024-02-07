package com.caseyjbrooks.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * A class that encapsulates the Koin module(s) provided by each Gradle subprojects. Allows easy access for the
 * end-project to select the right modules for its own variant.
 */
public interface KoinModule {

    /**
     * If this project needs to provide platform-specific components, the KoinModule in commonMain can refer itself to
     * an additional actual/expect module with those additional definitions.
     */
    public fun platformKoinModule(): KoinModule? = null

    /**
     * If there are other modules related to this one, they can be provided here. Those modules will be resolved against
     * the same variant as the main one.
     */
    public fun relatedModules(): List<KoinModule> = emptyList()

    /**
     * The main module which much be included in all application variants.
     */
    public fun mainModule(): Module = module { }

    /**
     * An additional, optional module to be provided for Unit Tests only.
     */
    public fun testModule(): Module = module { }

    /**
     * An additional, optional module to be provided for LOCAL builds only.
     */
    public fun localModule(): Module = module { }

    /**
     * An additional, optional module to be provided for QA builds only.
     */
    public fun qaModule(): Module = module { }

    /**
     * An additional, optional module to be provided for PROD builds only.
     */
    public fun productionModule(): Module = module { }

    /**
     * An additional, optional module to be provided for debug builds only.
     */
    public fun debugModule(): Module = module { }

    /**
     * An additional, optional module to be provided for release builds only.
     */
    public fun releaseModule(): Module = module { }
}
