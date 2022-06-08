package com.copperleaf.scripturenow.repositories.verses

import com.benasher44.uuid.Uuid
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import kotlinx.coroutines.flow.Flow

interface MemoryVersesRepository {

    fun getAllVerses(refreshCache: Boolean = false): Flow<Cached<List<MemoryVerse>>>

    fun getVerse(uuid: Uuid,refreshCache: Boolean = false): Flow<Cached<MemoryVerse>>

    suspend fun createOrUpdateVerse(verse: MemoryVerse)

    suspend fun deleteVerse(verse: MemoryVerse)

}
