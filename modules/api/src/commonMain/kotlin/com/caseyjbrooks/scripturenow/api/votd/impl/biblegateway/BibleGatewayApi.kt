package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway

import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models.BibleGatewayVotdResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

public interface BibleGatewayApi {

    @GET("usage/votd/rss/votd.rdf?31")
    @Headers("accept: application/xml")
    public suspend fun getVerseOfTheDay(): BibleGatewayVotdResponse
}
