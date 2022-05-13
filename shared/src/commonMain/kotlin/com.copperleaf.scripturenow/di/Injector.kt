package com.copperleaf.scripturenow.di

import com.copperleaf.scripturenow.votd.OurMannaApiBaseUrlKey
import com.copperleaf.scripturenow.votd.VerseOfTheDayRepository
import com.copperleaf.scripturenow.votd.votdModule
import com.squareup.sqldelight.db.SqlDriver
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.copper_leaf.shared.BASE_URL_OURMANNA
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindConstant
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

class Injector(
    engineFactory: HttpClientEngineFactory<*>,
    sqlDriver: SqlDriver,
) {
    private val kodein = DI.direct {
        bindConstant<String>(tag = OurMannaApiBaseUrlKey) { BASE_URL_OURMANNA }
        bind<HttpClient> {
            singleton {
                HttpClient(engineFactory) {
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

        import(votdModule)
    }

    val verseOfTheDayRepository get() = kodein.instance<VerseOfTheDayRepository>()
}
