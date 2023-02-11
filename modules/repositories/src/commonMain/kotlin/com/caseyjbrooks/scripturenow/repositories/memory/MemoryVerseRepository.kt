package com.caseyjbrooks.scripturenow.repositories.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

public interface MemoryVerseRepository {

    public fun getAllVerses(refreshCache: Boolean = false): Flow<Cached<List<MemoryVerse>>>

    public fun getVerse(uuid: Uuid, refreshCache: Boolean = false): Flow<Cached<MemoryVerse>>

    public suspend fun createOrUpdateVerse(verse: MemoryVerse)

    public suspend fun saveAsMemoryVerse(verse: VerseOfTheDay)

    public suspend fun deleteVerse(verse: MemoryVerse)
}
