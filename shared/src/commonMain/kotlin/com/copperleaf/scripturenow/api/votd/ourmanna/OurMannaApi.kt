package com.copperleaf.scripturenow.api.votd.ourmanna

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface OurMannaApi {

    @GET("get")
    suspend fun getVerseOfTheDay(
        @Query("format") format: String = "json",
        @Query("order") order: String = "daily",
    ): OurMannaVotdResponse
}
