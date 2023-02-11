@file:OptIn(ExperimentalSerializationApi::class)

package com.caseyjbrooks.scripturenow.api

import co.touchlab.kermit.Logger
import com.caseyjbrooks.scripturenow.config.LocalAppConfig
import com.caseyjbrooks.scripturenow.utils.Html
import com.caseyjbrooks.scripturenow.utils.KermitKtorLogger
import com.caseyjbrooks.scripturenow.utils.html
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
@OptIn(ExperimentalXmlUtilApi::class)
public object HttpClientProvider {
    public val json: Json = Json
    public val html: Html = Html()
    public val xml: XML = XML {
        unknownChildHandler = UnknownChildHandler { input, inputKind, descriptor, name, candidates ->
            // ignore it
            emptyList()
        }
    }

    public fun get(
        engine: HttpClientEngineFactory<*>? = OkHttp,
        config: LocalAppConfig,
    ): HttpClient {
        return if (engine != null) {
            HttpClient(engine) {
                configureEngine(config)
            }
        } else {
            HttpClient() {
                configureEngine(config)
            }
        }
    }

    private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.configureEngine(
        config: LocalAppConfig,
    ) {
        if (config.logApiCalls) {
            install(Logging) {
                this.level = LogLevel.BODY
                this.logger = KermitKtorLogger(Logger.withTag("${config.logPrefix} - HTTP"))
            }
        }
        install(ContentNegotiation) {
            json(json = json)
            xml(format = xml, contentType = ContentType.Text.Xml)
            xml(format = xml, contentType = ContentType.Application.Xml)
            html(format = html)
        }
    }
}
