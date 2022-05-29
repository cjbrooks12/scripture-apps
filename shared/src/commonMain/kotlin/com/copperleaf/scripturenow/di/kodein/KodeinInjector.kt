package com.copperleaf.scripturenow.di.kodein

import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.instance

class KodeinInjector(
    private val di: DirectDI
) : Injector {
    override val mainRouter: MainRouterViewModel get() = di.instance()
    override fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel = di.instance(arg = coroutineScope)

    companion object {
        fun create(
            engineFactory: HttpClientEngineFactory<*>,
            sqlDriver: SqlDriver,
            onBackstackEmptied: () -> Unit = { },
        ): Injector  {
            return KodeinInjector(
                DI.direct {
                    import(mainModule(engineFactory, sqlDriver, onBackstackEmptied))
                    import(votdApiModule())
                    import(votdDbModule())
                    import(votdRepositoryModule())
                    import(votdVmModule())
                }
            )
        }
    }
}
