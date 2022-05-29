package com.copperleaf.scripturenow.di.kodein

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.EventBusImpl
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel
import com.squareup.sqldelight.db.SqlDriver
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

const val RepositoryCoroutineScope = "RepositoryCoroutineScope"
const val RepositoryConfigBuilder = "RepositoryConfigBuilder"

fun mainModule(
    engineFactory: HttpClientEngineFactory<*>,
    sqlDriver: SqlDriver,
    onBackstackEmptied: () -> Unit = { },
) = DI.Module(name = "main") {
    bind<HttpClientEngineFactory<*>> {
        singleton { engineFactory }
    }
    bind<HttpClient> {
        singleton {
            HttpClient(instance<HttpClientEngineFactory<*>>()) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }
    }
    bind<Ktorfit> {
        factory { arg: String ->
            Ktorfit(
                baseUrl = arg,
                httpClient = instance()
            )
        }
    }
    bind<SqlDriver> {
        singleton { sqlDriver }
    }
    bind<MainRouterViewModel> {
        singleton {
            MainRouterViewModel(
                instance(tag = RepositoryCoroutineScope),
                instance(tag = RepositoryConfigBuilder),
                onBackstackEmptied
            )
        }
    }

    bind<CoroutineScope>(tag = RepositoryCoroutineScope) {
        singleton { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    }
    bind<EventBus>() {
        singleton { EventBusImpl() }
    }
    bind<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder) {
        provider {
            BallastViewModelConfiguration.Builder()
                .apply {
                    this += LoggingInterceptor()
                    logger = { PrintlnLogger(it) }

//                    this += BallastDebuggerInterceptor(instance<BallastDebuggerClientConnection<*>>())
                }
        }
    }
}
