package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom

import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models.DotComVotdResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

public interface DotComApi {

    @GET(" ")
    @Headers("accept: text/html")
    public suspend fun getVerseOfTheDay(): DotComVotdResponse
}
