package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna

import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models.OurMannaVotdResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

public interface OurMannaApi {

    @GET("get")
    @Headers("accept: application/json")
    public suspend fun getVerseOfTheDay(
        @Query("format") format: String = "json",
        @Query("order") order: String = VerseOfTheDayOrder.daily.name,
    ): OurMannaVotdResponse

    public enum class VerseOfTheDayOrder { daily, random }
}
