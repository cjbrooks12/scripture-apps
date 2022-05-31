package com.copperleaf.scripturenow.di.kodein

import com.copperleaf.scripturenow.api.mainApiModule
import com.copperleaf.scripturenow.api.votd.votdApiModule
import com.copperleaf.scripturenow.db.mainDbModule
import com.copperleaf.scripturenow.db.verses.memoryVersesDbModule
import com.copperleaf.scripturenow.db.votd.votdDbModule
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.repositories.mainRepositoryModule
import com.copperleaf.scripturenow.repositories.verses.memoryVersesRepositoryModule
import com.copperleaf.scripturenow.repositories.votd.VotdInterceptor
import com.copperleaf.scripturenow.repositories.votd.votdRepositoryModule
import com.copperleaf.scripturenow.ui.mainUiModule
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.router.routerVmModule
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import com.copperleaf.scripturenow.ui.votd.votdVmModule
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindSet
import org.kodein.di.instance

class KodeinInjector(
    private val di: DirectDI
) : Injector {
    override val mainRouter: MainRouterViewModel get() = di.instance()
    override fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel = di.instance(arg = coroutineScope)

    companion object {
        fun create(
            onBackstackEmptied: () -> Unit = { },
            additionalConfig: (DI.MainBuilder.() -> Unit)? = null
        ): Injector {
            return KodeinInjector(
                DI.direct {
                    bindSet<VotdInterceptor>()

                    // Application
                    import(mainApplicationModule())
                    import(platformApplicationModule())
                    additionalConfig?.invoke(this)

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

                    // UI
                    import(mainUiModule())
                    import(routerVmModule(onBackstackEmptied))
                    import(votdVmModule())
                }
            )
        }
    }
}
