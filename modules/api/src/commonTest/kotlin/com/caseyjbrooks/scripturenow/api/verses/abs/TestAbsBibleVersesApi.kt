package com.caseyjbrooks.scripturenow.api.verses.abs

import com.caseyjbrooks.scripturenow.api.verses.impl.abs.models.*
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path

public interface TestAbsBibleVersesApi {
    @GET("bibles?language=eng&include-full-details=false")
    @Headers("accept: application/json")
    public suspend fun getBibles(
        @Header("api-key") apiKey: String
    ): AbsBiblesResponse

    @GET("bibles/{bibleId}/books")
    @Headers("accept: application/json")
    public suspend fun getBooks(
        @Header("api-key") apiKey: String,
        @Path("bibleId") bibleId: String,
    ): AbsBooksResponse

    @GET("bibles/{bibleId}/books/{bookId}/chapters")
    @Headers("accept: application/json")
    public suspend fun getChapters(
        @Header("api-key") apiKey: String,
        @Path("bibleId") bibleId: String,
        @Path("bookId") bookId: String,
    ): AbsChaptersResponse

    @GET("bibles/{bibleId}/chapters/{bookChapterId}/verses")
    @Headers("accept: application/json")
    public suspend fun getVerses(
        @Header("api-key") apiKey: String,
        @Path("bibleId") bibleId: String,
        @Path("chapterId") chapterId: String,
    ): AbsVersesResponse

    @GET("bibles/{bibleId}/verses/{verseId}")
    @Headers("accept: application/json")
    public suspend fun getVerseContent(
        @Header("api-key") apiKey: String,
        @Path("bibleId") bibleId: String,
        @Path("verseId") verseId: String,
    ): AbsVerseContentResponse
}
