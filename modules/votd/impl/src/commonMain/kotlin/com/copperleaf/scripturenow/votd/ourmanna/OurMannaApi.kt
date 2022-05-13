package com.copperleaf.scripturenow.votd.ourmanna

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.QueryName

interface OurMannaApi {

    @GET("get")
    suspend fun getVerseOfTheDay(
        @Query("format") format: String = "json",
        @QueryName order: String = "daily",
    ): OurMannaVotdResponse
}
