package com.copperleaf.scripturenow.verses

import com.copperleaf.scripturenow.verses.models.Verse
import com.copperleaf.scripturenow.verses.models.VerseStatus
import com.copperleaf.scripturenow.verses.models.VerseTag
import kotlinx.coroutines.flow.Flow

interface VerseOfTheDayApi {

    suspend fun getAllVerses(
        status: VerseStatus? = null,
        tag: VerseTag? = null,
    ): Flow<List<Verse>>

    suspend fun createVerse(verse: Verse)

    suspend fun updateVerse(verse: Verse)

    suspend fun deleteVerse(verse: Verse)

}
