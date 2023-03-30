package com.caseyjbrooks.scripturenow.api.votd

import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.BibleGatewayApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.BibleGatewayApiImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.DotComApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.DotComApiImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.OurMannaApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.OurMannaApiImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.TheySaidSoApiConverterImpl
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.TheySaidSoApiImpl
import com.caseyjbrooks.scripturenow.config.local.LocalAppConfig
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*

public object VerseOfTheDayApiProvider {
    public fun get(
        service: VerseOfTheDayService,
        config: LocalAppConfig,
        client: HttpClient,
    ): VerseOfTheDayApi {
        return when (service) {
            VerseOfTheDayService.VerseOfTheDayDotCom -> {
                DotComApiImpl(
                    api = Ktorfit.Builder()
                        .baseUrl(config.verseOfTheDayDotComBaseUrl)
                        .httpClient(client)
                        .build()
                        .create(),
                    converter = DotComApiConverterImpl(),
                )
            }

            VerseOfTheDayService.BibleGateway -> {
                BibleGatewayApiImpl(
                    api = Ktorfit.Builder()
                        .baseUrl(config.bibleGatewayApiBaseUrl)
                        .httpClient(client)
                        .build()
                        .create(),
                    converter = BibleGatewayApiConverterImpl(),
                )
            }

            VerseOfTheDayService.OurManna -> {
                OurMannaApiImpl(
                    api = Ktorfit.Builder()
                        .baseUrl(config.ourMannaApiBaseUrl)
                        .httpClient(client)
                        .build()
                        .create(),
                    converter = OurMannaApiConverterImpl(),
                )
            }

            VerseOfTheDayService.TheySaidSo -> {
                TheySaidSoApiImpl(
                    api = Ktorfit.Builder()
                        .baseUrl(config.theySaidSoApiBaseUrl)
                        .httpClient(client)
                        .build()
                        .create(),
                    converter = TheySaidSoApiConverterImpl(),
                )
            }
        }
    }
}
