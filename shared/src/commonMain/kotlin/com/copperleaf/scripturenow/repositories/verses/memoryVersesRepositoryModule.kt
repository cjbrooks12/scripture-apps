package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.repositories.RepositoryConfigBuilder
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

typealias MemoryVerseInterceptor = BallastInterceptor<MemoryVersesRepositoryContract.Inputs, Any, MemoryVersesRepositoryContract.State>

fun memoryVersesRepositoryModule() = DI.Module(name = "Repository > Memory Verses") {
    bind<MemoryVersesRepository> {
        singleton {
            MemoryVersesRepositoryImpl(
                coroutineScope = instance(tag = RepositoryCoroutineScope),
                eventBus = instance(),
                inputHandler = MemoryVersesRepositoryInputHandler(
                    eventBus = instance(),
                    db = instance(),
                ),
                configBuilder = instance<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder)
                    .apply {
                        this += instance<Set<MemoryVerseInterceptor>>()
                    },
            )
        }
    }
}
