package com.copperleaf.scripturenow.di.kodein

import co.touchlab.kermit.Logger
import com.copperleaf.scripturenow.api.mainApiModule
import com.copperleaf.scripturenow.api.votd.votdApiModule
import com.copperleaf.scripturenow.db.mainDbModule
import com.copperleaf.scripturenow.db.verses.memoryVersesDbModule
import com.copperleaf.scripturenow.db.votd.votdDbModule
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.repositories.auth.AuthInterceptor
import com.copperleaf.scripturenow.repositories.auth.authRepositoryModule
import com.copperleaf.scripturenow.repositories.mainRepositoryModule
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel
import com.copperleaf.scripturenow.repositories.router.RouterInterceptor
import com.copperleaf.scripturenow.repositories.router.routerRepositoryModule
import com.copperleaf.scripturenow.repositories.verses.MemoryVerseInterceptor
import com.copperleaf.scripturenow.repositories.verses.memoryVersesRepositoryModule
import com.copperleaf.scripturenow.repositories.votd.VotdInterceptor
import com.copperleaf.scripturenow.repositories.votd.votdRepositoryModule
import com.copperleaf.scripturenow.ui.home.HomeViewModel
import com.copperleaf.scripturenow.ui.mainUiModule
import com.copperleaf.scripturenow.ui.settings.SettingsViewModel
import com.copperleaf.scripturenow.ui.settings.settingsUiModule
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsViewModel
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseViewModel
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListViewModel
import com.copperleaf.scripturenow.ui.verses.versesUiModule
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import com.copperleaf.scripturenow.ui.votd.votdVmModule
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindSet
import org.kodein.di.instance

class KodeinInjector(
    val di: DirectDI
) : Injector {
    override val mainRouter: MainRouterViewModel get() = di.instance()
    override fun logger(tag: String): Logger = di.instance(arg = tag)

    override fun homeViewModel(coroutineScope: CoroutineScope): HomeViewModel = di.instance(arg = coroutineScope)
    override fun settingsViewModel(coroutineScope: CoroutineScope): SettingsViewModel  = di.instance(arg = coroutineScope)
    override fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel = di.instance(arg = coroutineScope)
    override fun verseListViewModel(coroutineScope: CoroutineScope): MemoryVerseListViewModel = di.instance(arg = coroutineScope)
    override fun createOrEditVerseViewModel(coroutineScope: CoroutineScope): CreateOrEditMemoryVerseViewModel = di.instance(arg = coroutineScope)
    override fun verseDetailsViewModel(coroutineScope: CoroutineScope): MemoryVerseDetailsViewModel = di.instance(arg = coroutineScope)

    operator fun plus(builder: DI.MainBuilder.()->Unit): KodeinInjector {
        return KodeinInjector(
            DI.direct {
                extend(this@KodeinInjector.di, allowOverride = true)
                builder()
            }
        )
    }

    companion object {
        fun create(additionalConfig: DI.MainBuilder.()->Unit): KodeinInjector {
            return KodeinInjector(
                DI.direct() {
                    bindSet<VotdInterceptor>()
                    bindSet<MemoryVerseInterceptor>()
                    bindSet<RouterInterceptor>()
                    bindSet<AuthInterceptor>()

                    // Application
                    import(mainApplicationModule())
                    import(platformApplicationModule())
                    additionalConfig()

                    // API
                    import(mainApiModule())
                    import(votdApiModule())

                    // DB
                    import(mainDbModule())
                    import(votdDbModule())
                    import(memoryVersesDbModule())

                    // Repository
                    import(mainRepositoryModule())
                    import(votdRepositoryModule())
                    import(memoryVersesRepositoryModule())
                    import(authRepositoryModule())

                    // UI
                    import(mainUiModule())
                    import(settingsUiModule())
                    import(routerRepositoryModule())
                    import(votdVmModule())
                    import(versesUiModule())
                }
            )
        }
    }
}
