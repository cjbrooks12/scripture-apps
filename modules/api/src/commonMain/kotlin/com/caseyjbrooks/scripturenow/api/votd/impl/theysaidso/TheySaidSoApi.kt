package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso

import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

public interface TheySaidSoApi {

    @GET("bible/vod")
    @Headers("accept: application/json")
    public suspend fun getVerseOfTheDay(): TheySaidSoBibleVerseResponse

    @GET("bible/verse")
    @Headers("accept: application/json")
    public suspend fun getRandomVerse(): TheySaidSoBibleVerseResponse
}
