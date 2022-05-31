package com.copperleaf.scripturenow.repositories.votd

import com.copperleaf.scripturenow.repositories.RepositoryConfigBuilder
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun votdRepositoryModule() = DI.Module(name = "Repository > VOTD") {
    bind<VerseOfTheDayRepository> {
        singleton {
            VerseOfTheDayRepositoryImpl(
                coroutineScope = instance(tag = RepositoryCoroutineScope),
                eventBus = instance(),
                inputHandler = VerseOfTheDayRepositoryInputHandler(
                    eventBus = instance(),
                    api = instance(),
                    db = instance(),
                ),
                configBuilder = instance(tag = RepositoryConfigBuilder),
            )
        }
    }
}
