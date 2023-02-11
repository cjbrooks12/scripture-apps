package com.caseyjbrooks.scripturenow.repositories

import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepositoryProvider
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryProvider
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepositoryProvider
import com.caseyjbrooks.scripturenow.repositories.routing.ScriptureNowRouterProvider
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryProvider

public interface RepositoriesInjector :
    AuthRepositoryProvider,
    MemoryVerseRepositoryProvider,
    PrayerRepositoryProvider,
    ScriptureNowRouterProvider,
    VerseOfTheDayRepositoryProvider

public interface RepositoriesInjectorProvider {
    public fun getRepositoriesInjector(): RepositoriesInjector
}
