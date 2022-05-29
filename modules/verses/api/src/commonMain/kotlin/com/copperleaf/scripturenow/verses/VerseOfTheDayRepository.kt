package com.copperleaf.scripturenow.verses

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.verses.models.Verse
import com.copperleaf.scripturenow.verses.models.VerseStatus
import com.copperleaf.scripturenow.verses.models.VerseTag
import kotlinx.coroutines.flow.Flow

interface VerseOfTheDayRepository {

    fun clearAllCaches()

    fun getAllVerses(
        status: VerseStatus? = null,
        tag: VerseTag? = null,
        refreshCache: Boolean = false
    ): Flow<Cached<List<Verse>>>

    fun getVerse(
        id: Int,
        refreshCache: Boolean = false
    ): Flow<Cached<Verse>>

    suspend fun createVerse(verse: Verse)

    suspend fun updateVerse(verse: Verse)

    suspend fun softDeleteVerse(verse: Verse)

    suspend fun unDeleteVerse(verse: Verse)

    suspend fun hardDeleteVerse(verse: Verse)
}
