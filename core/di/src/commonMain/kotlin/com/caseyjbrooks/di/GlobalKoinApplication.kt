package com.caseyjbrooks.di

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.extension.coroutinesEngine
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.koinApplication

public object GlobalKoinApplication {
    private lateinit var _koinApplication: KoinApplication
    public val koin: Koin get() = _koinApplication.koin

    public fun init(block: KoinApplication.() -> Unit) {
        _koinApplication = koinApplication {
            coroutinesEngine()
            block()
        }
    }

    public inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T = koin.get(qualifier, parameters)

    public fun loadModules(vararg modules: Module) {
        koin.loadModules(modules.toList())
    }

    public fun unloadModules(vararg modules: Module) {
        koin.unloadModules(modules.toList())
    }
}
